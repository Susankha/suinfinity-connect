package com.suinfinity.user.util;

import jakarta.persistence.Embedded;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public enum RoleEnum {
  ADMIN(1),
  USER(2);

  private static final Map<Integer, RoleEnum> enumMap = new HashMap<>();

  static {
    for (RoleEnum enumConstant : RoleEnum.values()) {
      enumMap.put(enumConstant.getValue(), enumConstant);
    }
  }

  private final int value;

  RoleEnum(int value) {
    this.value = value;
  }

  public static RoleEnum fromInt(int intValue) {
    return enumMap.get(intValue);
  }
}
