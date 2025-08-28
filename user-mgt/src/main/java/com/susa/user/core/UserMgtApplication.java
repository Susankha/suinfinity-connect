package com.susa.user.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UserMgtApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserMgtApplication.class, args);
    log.info(" ========== Starting Suinfinity User Management ==========");
  }
}
