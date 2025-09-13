package com.suinfinity.user.mapper;

import com.suinfinity.user.dto.UserDTO;
import com.suinfinity.user.dto.UserResponseDTO;
import com.suinfinity.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User toUser(UserDTO userDTO);

  UserResponseDTO toUserResponseDto(User user);
}
