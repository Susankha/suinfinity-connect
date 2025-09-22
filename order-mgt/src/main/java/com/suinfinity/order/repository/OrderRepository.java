package com.suinfinity.order.repository;

import com.suinfinity.order.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Override
  List<Order> findAll();
}
