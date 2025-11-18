package com.suinfinity.user.service;

import com.suinfinity.user.dto.UserDTO;
import com.suinfinity.user.dto.UserResponseDTO;
import com.suinfinity.user.exception.UserNotFoundException;
import com.suinfinity.user.mapper.UserMapper;
import com.suinfinity.user.model.User;
import com.suinfinity.user.repository.RoleRepository;
import com.suinfinity.user.repository.UserRepository;
import com.suinfinity.user.util.RoleEnum;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class UserService implements UserDetailsService {

  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;

  public ResponseEntity<?> registerUser(UserDTO userDTO) {
    User mappedUser = UserMapper.INSTANCE.toUser(userDTO);
    long roleId = RoleEnum.valueOf(userDTO.getRole()).ordinal();
    mappedUser.setRoleId(roleId);
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

  public ResponseEntity<UserResponseDTO> getUser(String userName) {
    User user =
        userRepository
            .findByName(userName)
            .orElseThrow(
                () -> {
                  log.error("User '{}' does not exist ", userName);
                  return new UserNotFoundException(
                      "User " + "'" + userName + "'" + " does not exist");
                });

    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.toUserResponseDto(user);
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

  public ResponseEntity<UserResponseDTO> updateUser(String userName, UserDTO userDTO) {
    User user =
        userRepository
            .findByName(userName)
            .orElseThrow(
                () -> {
                  log.error("User '{}' does not exist, update operation failed ", userName);
                  return new UserNotFoundException(
                      "Update operation failed, User " + "'" + userName + "'" + " does not exist");
                });

    user.setName(userDTO.getName());
    user.setPassword(userDTO.getPassword());
    long roleId = RoleEnum.valueOf(userDTO.getRole()).ordinal();
    user.setRoleId(roleId);
    user.setEmail(userDTO.getEmail());
    user.setIsEnable(userDTO.getIsEnable());
    user.setAddress(userDTO.getAddress());
    try {
      userRepository.save(user);
    } catch (RuntimeException e) {
      throw new RuntimeException("User " + "'" + userName + "'" + " update failed");
    }
    UserResponseDTO userResponseDTO = UserMapper.INSTANCE.toUserResponseDto(user);
    return ResponseEntity.ok().body(userResponseDTO);
  }

  @Transactional
  public ResponseEntity<Void> deleteUser(String userName) {
    Long deletedCount = userRepository.deleteByName(userName);
    if (deletedCount == 0) {
      log.error("User '{}' does not exist, delete operation failed", userName);
      throw new UserNotFoundException(
          "Delete operation failed, User " + "'" + userName + "'" + " does not exist");
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  public UserDetails loadUserByUsername(String userName) {
    User user =
        userRepository
            .findByName(userName)
            .orElseThrow(
                () -> {
                  log.error("Load user failed,User '{}' does not exist ", userName);
                  return new UserNotFoundException(
                      "User " + "'" + userName + "'" + " does not exist");
                });
    return user;
  }
}
