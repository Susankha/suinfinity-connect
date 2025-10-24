package com.suinfinity.payment.service;

import com.suinfinity.payment.dto.PaymentDTO;
import com.suinfinity.payment.dto.PaymentResponseDTO;
import com.suinfinity.payment.exception.PaymentNotFoundException;
import com.suinfinity.payment.mapper.PaymentMapper;
import com.suinfinity.payment.model.Payment;
import com.suinfinity.payment.repository.PaymentRepository;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
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
    long paymentId = payment.getPaymentId();
    try {
      paymentRepository.save(payment);
    } catch (RuntimeException e) {
      log.error("Process payment operation failed with payment id : {}", paymentId);
      throw new RuntimeException(
          "Process payment with id " + "'" + paymentId + "'" + " operation failed");
    }
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  public ResponseEntity<PaymentResponseDTO> getPayment(long paymentId) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(
                () -> {
                  log.info(
                      "Get payment operation failed, payment does not found with id :" + paymentId);
                  return new PaymentNotFoundException(
                      "Get payment operation failed, payment doesn't exists with id:" + paymentId);
                });
    PaymentResponseDTO paymentResponseDTO = PaymentMapper.INSTANCE.toPaymentResponseDTO(payment);
    return ResponseEntity.ok(paymentResponseDTO);
  }

  public ResponseEntity<List<PaymentResponseDTO>> getPayments() {
    List<Payment> payments = paymentRepository.findAll();
    if (payments.isEmpty()) {
      log.info("Get payments operation failed, payments does not exists");
      throw new PaymentNotFoundException("Get payments operation failed, payments doesn't exists");
    }
    List<PaymentResponseDTO> paymentResponseDTOS =
        payments.stream().map(PaymentMapper.INSTANCE::toPaymentResponseDTO).toList();
    return ResponseEntity.ok(paymentResponseDTOS);
  }

  public ResponseEntity<PaymentResponseDTO> updatePayment(long paymentId, PaymentDTO paymentDTO) {
    Payment payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(
                () -> {
                  log.info(
                      "Update payments operation failed, payment does not exists with id :"
                          + paymentId);
                  return new PaymentNotFoundException(
                      "Update payments operation failed, payment doesn't exists with id:"
                          + paymentId);
                });
    Date updatedDate =
        Date.from(Instant.now().atZone(ZoneOffset.UTC).toLocalDateTime().toInstant(ZoneOffset.UTC));
    payment.setPaymentDate(updatedDate);
    payment.setPaymentMethod(paymentDTO.getPaymentMethod());
    payment.setAmount(paymentDTO.getAmount());
    payment.setPaymentStatus(paymentDTO.getPaymentStatus());
    paymentRepository.save(payment);
    PaymentResponseDTO paymentResponseDTO = PaymentMapper.INSTANCE.toPaymentResponseDTO(payment);
    return ResponseEntity.ok(paymentResponseDTO);
  }

  public ResponseEntity<Void> deletePayment(long paymentId) {
    try {
      paymentRepository.deleteByPaymentId(paymentId);
    } catch (PaymentNotFoundException ex) {
      log.error("Payment id '{}' does not exist, delete operation failed", paymentId);
      throw new RuntimeException(
          "Delete payment operation failed, payment doesn't exists with id:" + paymentId);
    }
    return ResponseEntity.noContent().build();
  }
}
