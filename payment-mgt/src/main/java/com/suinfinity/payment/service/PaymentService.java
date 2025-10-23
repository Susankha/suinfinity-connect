package com.suinfinity.payment.service;

import com.suinfinity.payment.dto.PaymentDTO;
import com.suinfinity.payment.mapper.PaymentMapper;
import com.suinfinity.payment.model.Payment;
import com.suinfinity.payment.repository.PaymentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PaymentService {

  @Autowired private PaymentRepository paymentRepository;

  public ResponseEntity<?> makePayment(PaymentDTO paymentDTO) {
    Payment payment = PaymentMapper.INSTANCE.toPayment(paymentDTO);
    paymentRepository.save(payment);
    return null;
  }
}
