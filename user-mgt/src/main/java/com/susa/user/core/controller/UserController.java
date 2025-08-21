package com.susa.user.core.controller;

import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.model.User;
import com.susa.user.core.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired private UserService userService;

  @PostMapping("/users")
  public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
    System.out.println("Creating User " + userDTO.getName());
    boolean created = userService.createUser(userDTO);
    if (!created) {
      ResponseEntity.badRequest().body("User " + userDTO.getName() + " registration failed");
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("User " + userDTO.getName() + " successfully registered");
  }

  @GetMapping(value = "/users/{name}", produces = "application/json")
  public ResponseEntity<User> getUser(@NotBlank @PathVariable String name) throws Exception {
    User user = userService.getUser(name);
    return ResponseEntity.ok().body(user);
  }

  @GetMapping(value = "/users", produces = "application/json")
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.getUsers();
    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(users);
  }

  @PutMapping("/users/{name}")
  public ResponseEntity<User> updateUser(
      @NotBlank @PathVariable String name, @Valid @RequestBody UserDTO user) {
    User updatedUser = userService.updateUser(name, user);
    if (updatedUser == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(value = "/users/{name}", produces = "application/json")
  public ResponseEntity<Void> deleteUser(@NotBlank @PathVariable String name) {
    boolean isDelete = userService.deleteUser(name);
    if (!isDelete) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
