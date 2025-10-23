package com.suinfinity.payment.controller;

import com.suinfinity.payment.dto.PaymentDTO;
import com.suinfinity.payment.dto.PaymentResponseDTO;
import com.suinfinity.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

  @Autowired private PaymentService paymentService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> makePayment(@RequestBody PaymentDTO paymentDTO) {
    return paymentService.makePayment(paymentDTO);
  }

  @GetMapping(value = "/{payment-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable("payment-id") long paymentId) {
    return paymentService.getPayment(paymentId);
  }
}
