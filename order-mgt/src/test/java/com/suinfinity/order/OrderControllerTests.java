package com.suinfinity.order;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.dto.OrderResponseDTO;
import com.suinfinity.order.mapper.OrderMapper;
import com.suinfinity.order.model.Order;
import com.suinfinity.order.service.OrderService;
import com.suinfinity.order.util.OrderStatus;
import com.suinfinity.order.util.OrderUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
@DisplayName("Order Operations Test Suite")
public class OrderControllerTests {

  private static final BigDecimal ORDER_AMOUNT = BigDecimal.valueOf(250);
  private static final String USER_ID = "80";
  private static final String PRODUCT_ID = "productId";
  private static final String PRODUCT_ID_VALUE = "12";
  private static final String PRICE = "price";
  private static final String PRICE_VALUE = "25.30";
  private static final String QUANTITY = "quantity";
  private static final String QUANTITY_VALUE = "20";

  @MockitoBean OrderService orderService;
  @Autowired MockMvc mockMvc;

  @Test
  @DisplayName("PlaceOrder operation test")
  public void placeOrder_shouldReturns_statusCreated() throws Exception {
    OrderDTO orderDTO = this.getOrderDTO();
    String jsonRequest = getJsonPayload(orderDTO);
    Mockito.when(orderService.placeOrder(orderDTO))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("GetOrder operation test")
  public void getOrder_shouldReturns_Order() throws Exception {
    Order order = getOrder();
    OrderResponseDTO orderResponseDTO = OrderMapper.INSTANCE.toOrderResponseDTO(order);
    orderResponseDTO.setOrderItems(getOrderItems());
    Mockito.when(orderService.getOrder(order.getOrderId()))
        .thenReturn(ResponseEntity.ok().body(orderResponseDTO));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/orders/{order-id}", order.getOrderId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.amount", greaterThanOrEqualTo(1)))
        .andExpect(jsonPath("$.orderItems", hasSize(1)));
  }

  @Test
  @DisplayName("GetOrders operation test")
  public void getOrders_shouldReturns_Orders() throws Exception {
    List<Order> orders = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Order order = getOrder();
      orders.add(order);
    }
    List<OrderResponseDTO> orderResponseDTOS =
        orders.stream().map(OrderMapper.INSTANCE::toOrderResponseDTO).collect(Collectors.toList());
    for (OrderResponseDTO orderResponseDTO : orderResponseDTOS) {
      orderResponseDTO.setOrderItems(getOrderItems());
    }
    Mockito.when(orderService.getOrders()).thenReturn(ResponseEntity.ok().body(orderResponseDTOS));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/orders"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$", hasSize(5)))
        .andExpect(jsonPath("$[0].orderItems", hasSize(1)));
  }

  @Test
  @DisplayName("UpdateOrder operation test")
  public void updateOrder_shouldReturns_updatedOrder() throws Exception {
    OrderDTO orderDTO = this.getOrderDTO();
    String jsonRequest = getJsonPayload(orderDTO);
    Order order = OrderMapper.INSTANCE.toOrder(orderDTO);
    order.setOrderId(Math.abs(RandomGenerator.getDefault().nextLong()));
    order.setAmount(BigDecimal.valueOf(200));
    OrderResponseDTO orderResponseDTO = OrderMapper.INSTANCE.toOrderResponseDTO(order);
    orderResponseDTO.setOrderItems(getOrderItems());
    Mockito.when(orderService.updateOrder(order.getOrderId(), orderDTO))
        .thenReturn(ResponseEntity.ok().body(orderResponseDTO));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/v1/orders/{order-id}", order.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount", comparesEqualTo(200)))
        .andExpect(jsonPath("$.orderItems", hasSize(1)));
  }

  @Test
  @DisplayName("GetOrderStatus operation test")
  public void getOrderStatus_shouldReturns_orderStatus() throws Exception {
    long orderId = Math.abs(RandomGenerator.getDefault().nextLong());
    Mockito.when(orderService.getOrderStatus(orderId))
        .thenReturn(ResponseEntity.ok().body(OrderStatus.PROCESSING.toString()));
    mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/orders/{order-id}/status", orderId))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("PROCESSING"));
  }

  @Test
  @DisplayName("UpdateOrderStatus operation test")
  public void updateOrderStatus_shouldReturns_updatedStatus() throws Exception {
    long orderId = Math.abs(RandomGenerator.getDefault().nextLong());
    Mockito.when(orderService.updateOrderStatus(orderId, OrderStatus.PROCESSING.toString()))
        .thenReturn(ResponseEntity.ok().build());
    mockMvc
        .perform(
            MockMvcRequestBuilders.patch(
                "/v1/orders/{order-id}/status/{order-status}",
                orderId,
                OrderStatus.PROCESSING.toString()))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("DeleteOrder operation test")
  public void deleteOrder_shouldReturns_statusDeleted() throws Exception {
    long orderId = Math.abs(RandomGenerator.getDefault().nextLong());
    Mockito.when(orderService.deleteOrder(orderId)).thenReturn(ResponseEntity.noContent().build());
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/v1/orders/{order-id}", orderId))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private OrderDTO getOrderDTO() {
    return new OrderDTO(ORDER_AMOUNT, USER_ID, getOrderItems());
  }

  private Order getOrder() {
    Order order = new Order();
    order.setOrderId(Math.abs(RandomGenerator.getDefault().nextLong()));
    order.setOrderDate(OrderUtil.getCurrentDateTime());
    order.setAmount(BigDecimal.valueOf(100));
    order.setStatus(OrderStatus.NEW.toString());
    order.setUserid(Math.abs(RandomGenerator.getDefault().nextLong()));
    return order;
  }

  private List<Map<String, String>> getOrderItems() {
    Map<String, String> orderItem = new HashMap<>();
    orderItem.put(PRODUCT_ID, PRODUCT_ID_VALUE);
    orderItem.put(PRICE, PRICE_VALUE);
    orderItem.put(QUANTITY, QUANTITY_VALUE);
    List<Map<String, String>> orderItemList = new ArrayList<>();
    orderItemList.add(orderItem);
    return orderItemList;
  }

  private String getJsonPayload(Object order) throws JsonProcessingException {
    ObjectMapper orderMapper = new ObjectMapper();
    orderMapper.registerModule(new JavaTimeModule());
    return orderMapper.writeValueAsString(order);
  }
}
