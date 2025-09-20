package com.suinfinity.user.model;

//import com.suinfinity.order.model.Order;
import com.suinfinity.user.dto.UserDTO.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "User")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  private String name;

  @Embedded private Address address;

  //fetch = FetchType.LAZY
  // @JoinColumn(name = "u_id")
//  @OneToMany(cascade = CascadeType.ALL)
//  List<Order> orders;
}
