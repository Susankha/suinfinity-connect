package com.susa.user.core.mapper;

import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.model.User;

public class UserMapper {

  public User mapUserDTOtoUser(UserDTO userDTO) {
    User user;
    user = new User();
    user.setName(userDTO.getName());
    user.setAddress(userDTO.getAddress());

    return user;
  }
}
