package com.suinfinity.product.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ProductMgtApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductMgtApplication.class);
    log.info("========== Starting Suinfinity Product Management ==========");
  }
}
