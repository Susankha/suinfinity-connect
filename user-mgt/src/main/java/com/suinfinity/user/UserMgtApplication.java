package com.suinfinity.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.suinfinity.iam", "com.suinfinity.user"})
public class UserMgtApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserMgtApplication.class, args);
    log.info(" ========== Starting Suinfinity User Management ==========");
  }
}
