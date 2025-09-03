package com.suinfinity.product.core.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
      regexp = "^[A-Za-z][A-Za-z0-9_]{7,19}$",
      message =
          "product name must start with a letter and contain only 8 to 20 alphanumeric characters or underscores")
  private String productName;

  @NotBlank(message = "description cannot be empty")
  @Pattern(
      regexp = "^[A-Za-z][A-Za-z0-9_]$",
      message =
          "description must start with a letter and contain only alphanumeric characters or underscores")
  private String description;

  @NotBlank(message = "price cannot be empty")
  @DecimalMin(value = "0.01", message = "price must be greater than zero")
  private BigDecimal price;

  @NotBlank(message = "stock quantity name cannot be empty")
  @PositiveOrZero(message = "stock quantity must be positive or zero")
  private long stockQuantity;
}
