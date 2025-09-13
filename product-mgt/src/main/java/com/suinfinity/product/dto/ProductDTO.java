package com.suinfinity.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {

  @NotBlank(message = "product name cannot be empty")
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_\\s]{7,19}+$",
      message =
          "product name must start with a letter and contain only 8 to 20 alphanumeric characters or underscores")
  private String name;

  @NotBlank(message = "description cannot be empty")
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_\\s]+$",
      message =
          "description must start with a letter and contain only alphanumeric characters or underscores")
  private String description;

  @NotNull(message = "product price cannot be empty")
  @Digits(
      integer = 10,
      fraction = 2,
      message = "price must have at most 10 integer digits and 2 decimal places")
  @DecimalMin(value = "0.01", message = "price must be greater than zero")
  private BigDecimal price;

  @PositiveOrZero(message = "stock quantity must be positive or zero")
  private long stockQuantity;
}
