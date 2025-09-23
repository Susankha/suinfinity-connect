package com.suinfinity.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class OrderDTO {

  private Date orderDate;
  private BigDecimal amount;
  private long user_id;

  public OrderDTO(BigDecimal amount, long user_id) {
    this.orderDate = Date.from(Instant.parse(getTimeStamp()));
    this.amount = amount;
    this.user_id = user_id;
  }

  private String getTimeStamp() {
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a 'UTC'");

    return Instant.now().atZone(ZoneOffset.UTC).format(dateTimeFormatter);
  }
}
