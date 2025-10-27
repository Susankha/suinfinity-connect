package com.suinfinity.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.Data;

@Data
public class PaymentDTO {

  @Positive(message = "Order id cannot be empty and must be a positive number")
  private long orderId;

  @NotNull(message = "Payment amount cannot be empty")
  @Digits(
      integer = 10,
      fraction = 2,
      message = "Payment amount must have at most 10 integer digits and 2 decimal places")
  @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
  private BigDecimal amount;

  @NotNull(message = "Payment method cannot be empty")
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_\\s]+$",
      message =
          "Payment status must start with a letter and contain only alphanumeric characters or underscores")
  private String paymentMethod;

  private LocalDateTime paymentDate;

  @NotNull(message = "Payment status cannot be empty")
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_\\s]+$",
      message =
          "Payment status must start with a letter and contain only alphanumeric characters or underscores")
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
