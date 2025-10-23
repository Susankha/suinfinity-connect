package com.suinfinity.payment.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.Data;

@Data
public class PaymentDTO {

  private long orderId;

  private BigDecimal amount;

  private String paymentMethod;

  private LocalDateTime paymentDate;

  private String paymentStatus;

  public PaymentDTO(String orderId, BigDecimal amount, String paymentMethod, String paymentStatus) {
    this.orderId = Long.parseLong(orderId);
    this.amount = amount;
    this.paymentMethod = paymentMethod;
    this.paymentDate = getTimeStamp();
    this.paymentStatus = paymentStatus;
  }

  private LocalDateTime getTimeStamp() {
    return Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime();
  }
}
