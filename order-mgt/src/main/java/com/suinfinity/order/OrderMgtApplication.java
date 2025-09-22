package com.suinfinity.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class OrderMgtApplication {
  public static void main(String[] args) {

    SpringApplication.run(OrderMgtApplication.class, args);
    log.info(" ========== Starting Suinfinity Order Management ==========");
  }
}
