package com.suinfinity.user.controller;

import com.suinfinity.user.dto.UserDTO;
import com.suinfinity.user.dto.UserResponseDTO;
import com.suinfinity.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

  @Autowired private UserService userService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('USER') or hasAnyAuthority('ALL','CREATE_USER')")
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
    return userService.registerUser(userDTO);
  }

  @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('USER') or hasAnyAuthority('ALL','READ_USER')")
  public ResponseEntity<UserResponseDTO> getUser(@NotBlank @PathVariable String name) {
    return userService.getUser(name);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('ALL')")
  public ResponseEntity<List<UserResponseDTO>> getUsers() {
    return userService.getUsers();
  }

  @PutMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyAuthority('ALL','UPDATE_USER')")
  public ResponseEntity<UserResponseDTO> updateUser(
      @NotBlank @PathVariable String name, @Valid @RequestBody UserDTO user) {
    return userService.updateUser(name, user);
  }

  @DeleteMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('ALL','DELETE_USER')")
  public ResponseEntity<?> deleteUser(@NotBlank @PathVariable String name) {
    return userService.deleteUser(name);
  }
}
