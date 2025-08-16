package com.susa.user.core.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

  @NotBlank(message = "Name cannot be empty")
  private String name;

  @Valid private Address address;

  public UserDTO(String name, Address address) {
    this.name = name;
    this.address = address;
  }

  @Data
  @Embeddable
  public static class Address {

    @NotBlank(message = "Street cannot be empty")
    private String street;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "ZipCode cannot be empty")
    private String zipCode;
  }
}
