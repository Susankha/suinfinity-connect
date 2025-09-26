package com.suinfinity.order.service;

import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.dto.OrderItemDTO;
import com.suinfinity.order.dto.OrderResponseDTO;
import com.suinfinity.order.exception.OrderNotFoundException;
import com.suinfinity.order.mapper.OrderItemMapper;
import com.suinfinity.order.mapper.OrderMapper;
import com.suinfinity.order.model.Order;
import com.suinfinity.order.model.OrderItem;
import com.suinfinity.order.repository.OrderItemRepository;
import com.suinfinity.order.repository.OrderRepository;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
    List<Map<String,String>> orderItems = orderDTO.getOrderItems();
    for (Map<String,String> orderItemMap : orderItems) {
      Set<?> keys = orderItemMap.keySet();
      Set<Entry<String, String>> entries = orderItemMap.entrySet();

//      OrderItemDTO orderItemDTO = orderItemMap.get("");
//      OrderItem orderItem = OrderItemMapper.INSTANCE.toOrderItem(orderItemDTO);
//      orderItemRepository.save(orderItem);
    }
    try {
      orderRepository.save(order);
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
    OrderResponseDTO orderResponseDTO = OrderMapper.INSTANCE.toOrderResponseDTO(order);
    return ResponseEntity.ok(orderResponseDTO);
  }

  public ResponseEntity<List<OrderResponseDTO>> getOrders() {
    List<Order> orders = orderRepository.findAll();
    List<OrderResponseDTO> orderResponseDTOS =
        orders.stream().map(OrderMapper.INSTANCE::toOrderResponseDTO).toList();
    if (orders.isEmpty()) {
      log.info("Orders are empty");
      return ResponseEntity.noContent().build();
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
    orderDTO.setOrderDate(Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime());
    order.setAmount(orderDTO.getAmount());
    try {
      orderRepository.save(order);
    } catch (RuntimeException e) {
      throw new RuntimeException("Order " + "'" + orderId + "'" + " update failed");
    }
    OrderResponseDTO orderResponseDTO = OrderMapper.INSTANCE.toOrderResponseDTO(order);
    return ResponseEntity.ok(orderResponseDTO);
  }

  @Transactional
  public ResponseEntity<Void> deleteOrder(long orderId) {
    orderRepository.deleteById(orderId);
    return ResponseEntity.noContent().build();
  }
}
