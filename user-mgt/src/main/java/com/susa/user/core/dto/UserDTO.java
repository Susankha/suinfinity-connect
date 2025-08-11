package com.susa.user.core.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
public class UserDTO {

  private String name;
  private Address address;

  public UserDTO(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  @Data
  @Embeddable
  public static class Address {

    private String street;
    private String city;
    private String zipCode;
  }
}
