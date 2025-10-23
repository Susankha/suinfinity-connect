package org.susa;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class PaymentMgtApplication {
  public static void main(String[] args) {
    SpringApplication.run(PaymentMgtApplication.class, args);
    log.info(" ========== Starting Suinfinity Payment Management ==========");
  }
}
