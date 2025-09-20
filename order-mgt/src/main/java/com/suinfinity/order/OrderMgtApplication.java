package com.suinfinity.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"com.suinfinity.user.repository"}) // Optional: if repositories are also in separate modules
//@EntityScan(basePackages = {"com.suinfinity.user.model"})
public class OrderMgtApplication {
  public static void main(String[] args) {

    SpringApplication.run(OrderMgtApplication.class, args);
    log.info(" ========== Starting Suinfinity Order Management ==========");
  }
}
