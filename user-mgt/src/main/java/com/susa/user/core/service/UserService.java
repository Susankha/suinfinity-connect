package com.susa.user.core.service;

import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.dto.UserResponseDTO;
import com.susa.user.core.mapper.UserMapper;
import com.susa.user.core.model.User;
import com.susa.user.core.repository.UserRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Log4j2
@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  public ResponseEntity<?> registerUser(UserDTO userDTO) throws RuntimeException {
    User mappedUser = UserMapper.INSTANCE.toUser(userDTO);
    try {
      userRepository.save(mappedUser);
    } catch (RuntimeException ex) {
      log.error("User {} registration failed ", mappedUser.getName());
      throw new RuntimeException(
          "User " + "'" + mappedUser.getName() + "'" + " registration failed");
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("User " + userDTO.getName() + " successfully registered");
  }

  public ResponseEntity<UserResponseDTO> getUser(String userName) throws NoResourceFoundException {
    User user = userRepository.findByName(userName);
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.toUserResponseDto(user);
    if (user == null) {
      log.error("User '{}' does not exist ", userName);
      throw new NoResourceFoundException(HttpMethod.GET, userName);
    }
    return ResponseEntity.ok().body(userResponseDTO);
  }

  public ResponseEntity<List<UserResponseDTO>> getUsers() {
    List<User> users = userRepository.findAll();
    List<UserResponseDTO> userResponseDTOS =
        users.stream().map(UserMapper.INSTANCE::toUserResponseDto).toList();
    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(userResponseDTOS);
  }

  public ResponseEntity<User> updateUser(String userName, UserDTO userDTO) {
    User user = userRepository.findByName(userName);
    try {
      user.setName(userDTO.getName());
      user.setAddress(userDTO.getAddress());
      userRepository.save(user);
    } catch (RuntimeException e) {
      throw new RuntimeException("User " + "'" + userName + "'" + " update failed");
    }
    return ResponseEntity.ok().body(user);
  }

  @Transactional
  public ResponseEntity<Void> deleteUser(String userName) {
    try {
      userRepository.deleteByName(userName);
    } catch (RuntimeException e) {
      throw new RuntimeException("User " + "'" + userName + "'" + " deletion failed");
    }
    return ResponseEntity.noContent().build();
  }
}
