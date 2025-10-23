package com.suinfinity.payment.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class PaymentResponseDTO {

  private long paymentId;
  private long orderId;
  private BigDecimal amount;
  private String paymentMethod;
  private Date paymentDate;
  private String paymentStatus;
}
