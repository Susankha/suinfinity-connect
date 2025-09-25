package com.suinfinity.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.Data;

@Data
public class OrderDTO {

  private LocalDateTime orderDate;

  @NotNull(message = "Order amount cannot be empty")
  @Digits(
      integer = 10,
      fraction = 2,
      message = "Order amount must have at most 10 integer digits and 2 decimal places")
  @DecimalMin(value = "0.01", message = "Order amount must be greater than zero")
  private BigDecimal amount;

  @NotNull(message = "User id cannot be empty")
  @Positive(message = "User id must be a positive number")
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
