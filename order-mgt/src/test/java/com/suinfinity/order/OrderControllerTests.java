package com.suinfinity.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.suinfinity.order.dto.OrderDTO;
import com.suinfinity.order.model.Order;
import com.suinfinity.order.service.OrderService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;
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
  private static final String PRICE = "25.price";
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

  private OrderDTO getOrderDTO() {
    return new OrderDTO(ORDER_AMOUNT, USER_ID, this.getOrderItems());
  }

  private Order getOrder() {
    Order order = new Order();
    order.setOrderId(RandomGenerator.getDefault().nextLong());
    order.setOrderDate(
        Date.from(
            Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime().toInstant(ZoneOffset.UTC)));
    order.setAmount(BigDecimal.valueOf(100));
    order.setUserid(RandomGenerator.getDefault().nextLong());
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
