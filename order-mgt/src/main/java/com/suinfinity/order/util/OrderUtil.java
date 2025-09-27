package com.suinfinity.order.util;

import com.suinfinity.order.dto.OrderItemDTO;
import java.math.BigDecimal;
import java.util.Map;

public class OrderUtil {
  private static final String PRODUCT_ID = "productId";
  private static final String PRICE = "price";
  private static final String QUANTITY = "quantity";

  public static OrderItemDTO getOrderItemDTO(Map<String, String> orderItemMap) {
    String productId = orderItemMap.get(PRODUCT_ID);
    String price = orderItemMap.get(PRICE);
    String quantity = orderItemMap.get(QUANTITY);
    OrderItemDTO orderItemDTO =
        new OrderItemDTO(
            Long.parseLong(productId), new BigDecimal(price), Long.parseLong(quantity));
    return orderItemDTO;
  }
}
