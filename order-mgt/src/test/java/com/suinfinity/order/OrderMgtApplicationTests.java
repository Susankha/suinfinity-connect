package com.suinfinity.order;

import com.suinfinity.order.controller.OrderController;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
@DisplayName("Order Management Application Tests")
public class OrderMgtApplicationTests {
  @Autowired OrderController orderController;

  @Test
  void contextLoad() {
    log.info("Order Mgt application test");
    Assertions.assertNotNull(orderController, "Order Mgt App context loading failed");
  }
}
