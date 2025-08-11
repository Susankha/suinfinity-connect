package com.susa.user.core.service;

import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.mapper.UserMapper;
import com.susa.user.core.model.User;
import com.susa.user.core.repository.UserRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  Logger logger = LoggerFactory.getLogger(UserService.class);
  @Autowired private UserRepository userRepository;

  public boolean createUser(UserDTO userDTO) {
    UserMapper userMapper = new UserMapper();
    User mappedUser = userMapper.mapUserDTOtoUser(userDTO);
    User savedUser = userRepository.save(mappedUser);
    if (savedUser.getUserId() == null) {
      logger.error("User {} creation failed ", mappedUser.getName());
      return false;
    }
    return true;
  }

  public User getUser(String userName) {
    User user = userRepository.findByName(userName);
    if (user == null) {
      logger.error("User {} does not exist ", userName);
      return null;
    }
    return user;
  }

  public List<User> getUsers() {
    List<User> users = userRepository.findAll();
    if (users.isEmpty()) {
      return null;
    }
    return users;
  }

  public User updateUser(String userName, UserDTO userDTO) {
    User user = userRepository.findByName(userName);
    user.setName(userDTO.getName());
    user.setAddress(userDTO.getAddress());
    user = userRepository.save(user);
    return user;
  }

  @Transactional
  public boolean deleteUser(String userName) {
    Long count = userRepository.deleteByName(userName);
    boolean isDelete = false;
    if (count > 0) {
      logger.info("User {} has deleted ", userName);
      isDelete = true;
    }
    return isDelete;
  }
}
