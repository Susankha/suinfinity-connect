package com.suinfinity.user.dto;

import com.suinfinity.user.dto.UserDTO.Address;
import com.suinfinity.user.model.Role;
import jakarta.persistence.Embedded;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class UserResponseDTO {

  private Long userId;
  private String name;
  private String password;
  private Set<Role> grantedRoles = new HashSet<>();
  private Set<GrantedAuthority> authorities = new HashSet<>();
  private String email;
  private Boolean isEnable;
  @Embedded private Address address;
}
