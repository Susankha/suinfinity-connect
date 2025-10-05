package com.suinfinity.order.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemResponseDTO {

  private long orderItemId;
  private long orderId;
  private long productId;
  private BigDecimal price;
  private long quantity;
}
