package com.suinfinity.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "OrderItems")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long orderItemId;

  @Column(name = "order_id")
  private long orderId;

  @Column(name = "product_id")
  private long productId;

  private long quantity;

  @Column(scale = 2)
  private BigDecimal price;
}
