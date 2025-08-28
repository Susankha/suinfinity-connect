package com.susa.user.core.service;

import com.susa.user.core.dto.UserDTO;
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
    UserMapper userMapper = new UserMapper();
    User mappedUser = userMapper.mapUserDTOtoUser(userDTO);
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

  public ResponseEntity<User> getUser(String userName) throws NoResourceFoundException {
    User user = userRepository.findByName(userName);
    if (user == null) {
      log.error("User '{}' does not exist ", userName);
      throw new NoResourceFoundException(HttpMethod.GET, userName);
    }
    return ResponseEntity.ok().body(user);
  }

  public ResponseEntity<List<User>> getUsers() {
    List<User> users = userRepository.findAll();
    if (users.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok().body(users);
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
  public ResponseEntity<?> deleteUser(String userName) {
    try {
      userRepository.deleteByName(userName);
    } catch (RuntimeException e) {
      throw new RuntimeException("User " + "'" + userName + "'" + " deletion failed");
    }
    return ResponseEntity.status(HttpStatus.OK).body("User " + userName + " successfully deleted");
  }
}
