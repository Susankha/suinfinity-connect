package com.suinfinity.payment.service;

import com.suinfinity.payment.dto.PaymentDTO;
import com.suinfinity.payment.dto.PaymentResponseDTO;
import com.suinfinity.payment.mapper.PaymentMapper;
import com.suinfinity.payment.model.Payment;
import com.suinfinity.payment.repository.PaymentRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PaymentService {

  @Autowired private PaymentRepository paymentRepository;

  public ResponseEntity<?> makePayment(PaymentDTO paymentDTO) {
    Payment payment = PaymentMapper.INSTANCE.toPayment(paymentDTO);
    paymentRepository.save(payment);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  public ResponseEntity<PaymentResponseDTO> getPayment(long paymentId) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(
                () -> {
                  log.info("payment not found");
                  return new RuntimeException("payment not found with id:" + paymentId);
                });
    PaymentResponseDTO paymentResponseDTO = PaymentMapper.INSTANCE.toPaymentResponseDTO(payment);
    return ResponseEntity.ok(paymentResponseDTO);
  }

  public ResponseEntity<List<PaymentResponseDTO>> getPayments() {
    List<Payment> payments = paymentRepository.findAll();
    List<PaymentResponseDTO> paymentResponseDTOS =
        payments.stream().map(PaymentMapper.INSTANCE::toPaymentResponseDTO).toList();
    return ResponseEntity.ok(paymentResponseDTOS);
  }
}
