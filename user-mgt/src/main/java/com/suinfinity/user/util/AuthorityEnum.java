package com.suinfinity.user.util;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public enum AuthorityEnum {
  ALL(1),
  CREATE_USER(2),
  READ_USER(3),
  UPDATE_USER(4),
  DELETE_USER(5),
  CREATE_ORDER(6),
  READ_ORDER(7),
  UPDATE_ORDER(8),
  DELETE_ORDER(9);

  private static final Map<Integer, AuthorityEnum> enumMap = new HashMap<>();

  static {
    for (AuthorityEnum authorityEnum : AuthorityEnum.values()) {
      enumMap.put(authorityEnum.getValue(), authorityEnum);
    }
  }

  private final int value;

  AuthorityEnum(int value) {
    this.value = value;
  }

  public static AuthorityEnum fromInt(int intValue) {
    return enumMap.get(intValue);
  }
}
