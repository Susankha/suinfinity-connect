package com.suinfinity.payment.controller;

import com.suinfinity.payment.dto.PaymentDTO;
import com.suinfinity.payment.dto.PaymentResponseDTO;
import com.suinfinity.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

  @Autowired private PaymentService paymentService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> makePayment(@Valid @RequestBody PaymentDTO paymentDTO) {
    return paymentService.makePayment(paymentDTO);
  }

  @GetMapping(value = "/{payment-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaymentResponseDTO> getPayment(
      @NotNull @PathVariable("payment-id") long paymentId) {
    return paymentService.getPayment(paymentId);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PaymentResponseDTO>> getPayments() {
    return paymentService.getPayments();
  }

  @PutMapping(value = "/{payment-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaymentResponseDTO> updatePayment(
      @NotNull @PathVariable("payment-id") long paymentId,
      @Valid @RequestBody PaymentDTO paymentDTO) {
    return paymentService.updatePayment(paymentId, paymentDTO);
  }

  @DeleteMapping(value = "/{payment-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deletePayment(@NotNull @PathVariable("payment-id") long paymentId) {
    return paymentService.deletePayment(paymentId);
  }

  @GetMapping(value = "/{payment-id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getPaymentStatus(
      @NotNull @PathVariable("payment-id") long paymentId) {
    return paymentService.getPaymentStatus(paymentId);
  }

  @PatchMapping(
      value = "/{payment-id}/status/{payment-status}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
      @NotNull @PathVariable("payment-id") long paymentId,
      @NotNull @PathVariable("payment-status") String paymentStatus) {
    return paymentService.updatePaymentStatus(paymentId, paymentStatus);
  }
}
