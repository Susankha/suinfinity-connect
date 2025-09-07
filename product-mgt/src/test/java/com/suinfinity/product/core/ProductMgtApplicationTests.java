package com.suinfinity.product.core;

import com.suinfinity.product.core.controller.ProductController;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
public class ProductMgtApplicationTests {

  @Autowired ProductController productController;

  void contextLoad() {
    Assertions.assertNotNull(productController, "ProductMgt App context loading failed");
  }
}
