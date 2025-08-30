package com.susa.user.core.mapper;

import com.susa.user.core.dto.UserDTO;
import com.susa.user.core.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User toUser(UserDTO userDTO);

  UserDTO toUserDto(User user);
}
