package com.suinfinity.order;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class OrderMgtApplication {
  public static void main(String[] args) {

    SpringApplication.run(OrderMgtApplication.class, args);
    log.info(" ========== Starting Suinfinity Order Management ==========");
  }
}
