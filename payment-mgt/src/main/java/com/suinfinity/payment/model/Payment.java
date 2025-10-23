package com.suinfinity.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Payments")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long paymentId;

  @Column(name = "order_id")
  private long orderId;

  @Column(scale = 2)
  private BigDecimal amount;

  private String paymentMethod;

  @Temporal(TemporalType.TIMESTAMP)
  private Date paymentDate;

  private String paymentStatus;
}
