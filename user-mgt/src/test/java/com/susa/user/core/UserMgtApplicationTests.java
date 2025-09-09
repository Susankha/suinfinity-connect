package com.susa.user.core;

import com.susa.user.core.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class UserMgtApplicationTests {

  @Autowired UserController userController;

  void contextLoads() {
    Assertions.assertNotNull(userController, "UserMgt App context loading failed");
  }
}
