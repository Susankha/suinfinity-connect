package com.susa.user.core.dto;

import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

  @NotBlank(message = "Name cannot be empty")
  @Size(min = 1, max = 20, message = "Name should have a length between 2 to 20")
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
    @Size(min = 1, max = 20, message = "Street should have a length between 2 to 20")
    private String street;

    @NotBlank(message = "City cannot be empty")
    @Size(min = 1, max = 20, message = "City should have a length between 2 to 20")
    private String city;

    @NotBlank(message = "ZipCode cannot be empty")
    @Pattern(regexp = "^[0-9]{5}", message = "ZipCode must have 5 digits")
    private String zipCode;
  }
}
