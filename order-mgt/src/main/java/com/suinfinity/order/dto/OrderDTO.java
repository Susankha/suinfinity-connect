package com.suinfinity.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.Data;

@Data
public class OrderDTO {

  private LocalDateTime orderDate;
  private BigDecimal amount;
  private long userid;

  public OrderDTO(BigDecimal amount, String userid) {
    this.orderDate = getTimeStamp();
    this.amount = amount;
    this.userid = Long.parseLong(userid);
  }

  private LocalDateTime getTimeStamp() {
    return Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime();
  }
}
