package com.suinfinity.order.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class OrderResponseDTO {

  private Long orderId;
  private Date orderDate;
  private BigDecimal amount;
  private long userid;
}
