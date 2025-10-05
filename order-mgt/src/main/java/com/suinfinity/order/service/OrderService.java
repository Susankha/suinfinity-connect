package com.suinfinity.order.service;

import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.dto.OrderItemDTO;
import com.suinfinity.order.dto.OrderItemResponseDTO;
import com.suinfinity.order.dto.OrderResponseDTO;
import com.suinfinity.order.exception.OrderNotFoundException;
import com.suinfinity.order.mapper.OrderItemMapper;
import com.suinfinity.order.mapper.OrderMapper;
import com.suinfinity.order.model.Order;
import com.suinfinity.order.model.OrderItem;
import com.suinfinity.order.repository.OrderItemRepository;
import com.suinfinity.order.repository.OrderRepository;
import com.suinfinity.order.util.OrderUtil;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class OrderService {

  @Autowired private OrderRepository orderRepository;
  @Autowired private OrderItemRepository orderItemRepository;

  public ResponseEntity<?> placeOrder(OrderDTO orderDTO) {
    Order order = OrderMapper.INSTANCE.toOrder(orderDTO);
    List<Map<String, String>> orderItems = orderDTO.getOrderItems();
    try {
      orderRepository.save(order);
      this.saveOrderItems(orderItems, order);
    } catch (RuntimeException e) {
      log.error("Order {} placement failed ", order.getOrderId());
      throw new RuntimeException("Order " + "'" + order.getOrderId() + "'" + " placement failed");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body("Order " + " successfully placed");
  }

  public ResponseEntity<OrderResponseDTO> getOrder(long orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(
                () -> {
                  log.error("Order '{}' does not exist ", orderId);
                  return new OrderNotFoundException(
                      "Order " + "'" + orderId + "'" + " does not exist");
                });
    List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
    List<Map<String, String>> orderItemDTOS = this.getOrderItems(orderItemList);
    OrderResponseDTO orderResponseDTO = OrderMapper.INSTANCE.toOrderResponseDTO(order);
    orderResponseDTO.setOrderItems(orderItemDTOS);
    return ResponseEntity.ok(orderResponseDTO);
  }

  public ResponseEntity<List<OrderResponseDTO>> getOrders() {
    List<Order> orders = orderRepository.findAll();
    ListIterator<Order> listIterator = orders.listIterator();
    Map<Long, List<Map<String, String>>> orderItemDTOSMap = new HashMap<>();

    while (listIterator.hasNext()) {
      Order order = listIterator.next();
      long orderId = order.getOrderId();
      List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
      List<Map<String, String>> orderItemDTOS = this.getOrderItems(orderItemList);
      orderItemDTOSMap.put(orderId, orderItemDTOS);
    }
    List<OrderResponseDTO> orderResponseDTOS =
        orders.stream().map(OrderMapper.INSTANCE::toOrderResponseDTO).toList();
    for (OrderResponseDTO orderResponseDTO : orderResponseDTOS) {
      orderResponseDTO.setOrderItems(orderItemDTOSMap.get(orderResponseDTO.getOrderId()));
    }
    if (orders.isEmpty()) {
      log.info("Orders are empty");
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(orderResponseDTOS);
  }

  public ResponseEntity<OrderResponseDTO> updateOrder(long orderId, OrderDTO orderDTO) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(
                () -> {
                  log.error("Order '{}' does not exist, update operation failed ", orderId);
                  return new OrderNotFoundException(
                      "Update operation failed, Order " + "'" + orderId + "'" + " does not exist");
                });
    List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
    for (Map<String, String> orderItemMap : orderDTO.getOrderItems()) {
      orderItemDTOS.add(OrderUtil.getOrderItemDTO(orderItemMap));
    }

    List<OrderItem> exsistOrderItemList = orderItemRepository.findByOrderId(orderId);
    for (int i = 0; i < exsistOrderItemList.size(); i++) {
      OrderItem item = exsistOrderItemList.get(i);
      OrderItemDTO orderItemDTO = orderItemDTOS.get(i);
      item.setProductId(orderItemDTO.getProductId());
      item.setPrice(orderItemDTO.getPrice());
      item.setQuantity(orderItemDTO.getQuantity());
      orderItemRepository.save(item);
    }
    Date updatedDate =
        Date.from(Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime().toInstant(ZoneOffset.UTC));
    order.setOrderDate(updatedDate);
    order.setAmount(orderDTO.getAmount());
    orderRepository.save(order);
    log.info("Order updated successfully");
    List<Map<String, String>> updatedOrderItems = getOrderItems(exsistOrderItemList);
    OrderResponseDTO orderResponseDTO = OrderMapper.INSTANCE.toOrderResponseDTO(order);
    orderResponseDTO.setOrderItems(updatedOrderItems);
    return ResponseEntity.ok(orderResponseDTO);
  }

  @Transactional
  public ResponseEntity<Void> deleteOrder(long orderId) {
    long orderCount = orderRepository.deleteByOrderId(orderId);
    if (orderCount == 0) {
      log.error("Order id '{}' does not exist, delete operation failed", orderId);
      throw new RuntimeException(
          "Delete operation failed, order id:" + "'" + orderId + "'" + " does not exist");
    }
    long itemCount = orderItemRepository.deleteByOrderId(orderId);
    log.info("Deleted order item count is: '{}' :", itemCount);
    return ResponseEntity.noContent().build();
  }

  private List<Map<String, String>> getOrderItems(List<OrderItem> orderItemList) {
    ListIterator<OrderItem> listIterator = orderItemList.listIterator();
    List<Map<String, String>> orderItemResponseDTOList = new ArrayList<>();
    while (listIterator.hasNext()) {
      OrderItem orderItem = listIterator.next();
      OrderItemResponseDTO orderItemResponseDTO =
          OrderItemMapper.INSTANCE.toOrderItemDTO(orderItem);
      Map<String, String> orderItems = OrderUtil.setOrderItemDTO(orderItemResponseDTO);
      orderItemResponseDTOList.add(orderItems);
    }
    return orderItemResponseDTOList;
  }

  private void saveOrderItems(List<Map<String, String>> orderItems, Order order) {
    for (Map<String, String> orderItemMap : orderItems) {
      OrderItemDTO orderItemDTO = OrderUtil.getOrderItemDTO(orderItemMap);
      OrderItem orderItem = OrderItemMapper.INSTANCE.toOrderItem(orderItemDTO);
      orderItem.setOrderId(order.getOrderId());
      orderItem.setPrice(orderItem.getPrice());
      orderItem.setProductId(orderItem.getProductId());
      orderItem.setQuantity(orderItem.getQuantity());
      orderItemRepository.save(orderItem);
    }
  }
}
