package com.suinfinity.order.repository;

import com.suinfinity.order.exception.OrderNotFoundException;
import com.suinfinity.order.model.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  void deleteByOrderId(Long orderId) throws OrderNotFoundException;

  @Query("SELECT o.status FROM Order o WHERE o.orderId = :orderId")
  Optional<String> findOrderStatusByOrderId(Long orderId);
}
