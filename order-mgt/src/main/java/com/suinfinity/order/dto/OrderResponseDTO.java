package com.suinfinity.order.dto;

import com.suinfinity.order.util.OrderStatus;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class OrderResponseDTO {

  private Long orderId;
  private Date orderDate;
  private BigDecimal amount;
  private long userid;
  private OrderStatus status;
  private List<Map<String, String>> orderItems;
}
