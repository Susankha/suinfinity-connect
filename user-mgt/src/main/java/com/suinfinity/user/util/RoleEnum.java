package com.suinfinity.user.util;

import java.util.Optional;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public enum RoleEnum {
  ADMIN,
  USER;

  public static Optional<RoleEnum> getValueOf(String role) {
    try {
      return Optional.of(RoleEnum.valueOf(role.toUpperCase()));
    } catch (IllegalArgumentException e) {
      log.error("Role '{}' does not exist ", role);
      return Optional.empty();
    }
  }
}
