package com.susa.user.core.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {

  @NotBlank(message = "Name cannot be empty")
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_]{7,19}$",
      message =
          "Name must start with a letter and contain only 8 to 20 alphanumeric characters or underscores")
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
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z0-9_]{4,19}$",
        message = "Street should have a length between 5 to 20")
    private String street;

    @NotBlank(message = "City cannot be empty")
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z0-9_]{4,19}$",
        message = "City should have a length between 5 to 20")
    private String city;

    @NotBlank(message = "ZipCode cannot be empty")
    @Pattern(regexp = "^[0-9]{5}$", message = "ZipCode must have 5 digits")
    private String zipCode;
  }
}
