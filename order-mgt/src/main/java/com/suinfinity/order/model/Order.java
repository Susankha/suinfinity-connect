package com.suinfinity.order.model;

//import com.suinfinity.user.model.User;
import com.suinfinity.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long orderId;

  //fetch = FetchType.EAGER, cascade = CascadeType.ALL,
//  @ManyToOne(fetch = FetchType.LAZY, optional = false)
//  @JoinColumn(name = "user_id", nullable = false)
//  private User user;

  @Temporal(TemporalType.TIMESTAMP)
  private Date orderDate;

  @Column(scale = 2)
  private BigDecimal amount;
}
