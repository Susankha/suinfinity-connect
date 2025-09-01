package com.susa.user.core.dto;

import com.susa.user.core.dto.UserDTO.Address;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
public class UserResponseDTO {

  private Long userId;
  private String name;
  @Embedded private Address address;
}
