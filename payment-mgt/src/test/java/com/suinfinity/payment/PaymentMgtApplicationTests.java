package com.suinfinity.payment;

import com.suinfinity.payment.controller.PaymentController;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
@DisplayName("Payment Management Application Tests")
public class PaymentMgtApplicationTests {

  @Autowired PaymentController paymentController;

  void contextLoad() {
    log.info("Payment Mgt application test");
    Assertions.assertNotNull(paymentController, "Payment Mgt App context loading failed");
  }
}
