package com.suinfinity.payment.repository;

import com.suinfinity.payment.exception.PaymentNotFoundException;
import com.suinfinity.payment.model.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  void deleteByPaymentId(Long orderId) throws PaymentNotFoundException;

  @Query("SELECT p.paymentStatus FROM Payment p WHERE p.paymentId = :paymentId")
  Optional<String> findPaymentStatusByPaymentId(Long paymentId);
}
