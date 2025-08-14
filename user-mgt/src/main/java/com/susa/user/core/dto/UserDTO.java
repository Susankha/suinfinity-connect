package com.susa.user.core.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {

  @NotBlank (message = "Name cannot be empty")
  private String name;
  //@NotBlank (message = "Address cannot be empty")
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
