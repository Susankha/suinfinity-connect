package com.suinfinity.user.controller;

import java.security.Principal;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/login")
public class LoginController {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> login(Principal principal, Authentication authentication) {
    String user = principal.getName();
    String role = authentication.getName();
    return ResponseEntity.ok("login successfully by\n user:" + user + "\n with roles :" + role);
  }
}
