package com.suinfinity.user.dto;

import com.suinfinity.user.dto.UserDTO.Address;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
public class UserResponseDTO {

  private Long userId;
  private String name;
  private String password;
  private String role;
  private String email;
  private Boolean isEnable;
  @Embedded private Address address;
}
