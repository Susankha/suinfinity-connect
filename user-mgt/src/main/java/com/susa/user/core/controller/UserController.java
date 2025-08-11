package com.susa.user.core.controller;

import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.model.User;
import com.susa.user.core.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
    System.out.println("Creating User " + userDTO.getName());
    boolean created = userService.createUser(userDTO);
    if (!created) {
      ResponseEntity.badRequest().body("User " + userDTO.getName() + " creation failed");
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping("users/{name}")
  public ResponseEntity<User> getUser(@PathVariable String name) {
    User user = userService.getUser(name);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(user);
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userService.getUsers();
    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(users);
  }

  @PutMapping("users/{name}")
  public ResponseEntity<User> updateUser(@PathVariable String name, @RequestBody UserDTO user) {
    User updatedUser = userService.updateUser(name, user);
    if (updatedUser == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("users/{name}")
  public ResponseEntity<String> deleteUser(@PathVariable String name) {
    boolean isDelete = userService.deleteUser(name);
    if (!isDelete) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body("User " + name + " deleted");
  }
}
