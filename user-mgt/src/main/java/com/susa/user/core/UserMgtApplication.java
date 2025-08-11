package com.susa.user.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserMgtApplication {

  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(UserMgtApplication.class);
    SpringApplication.run(UserMgtApplication.class, args);
    logger.info("===== Susa log ===============");
  }
}
