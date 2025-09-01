package com.susa.user.core.controller;

import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.dto.UserResponseDTO;
import com.susa.user.core.model.User;
import com.susa.user.core.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired private UserService userService;

  @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
    return userService.registerUser(userDTO);
  }

  @GetMapping(value = "/get/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponseDTO> getUser(@NotBlank @PathVariable String name)
      throws Exception {
    return userService.getUser(name);
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserResponseDTO>> getUsers() {
    return userService.getUsers();
  }

  @PutMapping(value = "/update/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> updateUser(
      @NotBlank @PathVariable String name, @Valid @RequestBody UserDTO user) {
    return userService.updateUser(name, user);
  }

  @DeleteMapping(value = "/delete/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteUser(@NotBlank @PathVariable String name) {
    return userService.deleteUser(name);
  }
}
