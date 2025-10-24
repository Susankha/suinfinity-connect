package com.suinfinity.payment.repository;

import com.suinfinity.payment.exception.PaymentNotFoundException;
import com.suinfinity.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  void deleteByPaymentId(Long orderId) throws PaymentNotFoundException;
}
