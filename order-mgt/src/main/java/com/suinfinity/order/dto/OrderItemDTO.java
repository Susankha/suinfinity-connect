package com.suinfinity.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {

  @NotNull(message = "Product id cannot be empty")
  @Positive(message = "Product id must be a positive number")
  private long productId;

  @NotNull(message = "Product price cannot be empty")
  @Positive(message = "Product price must be a positive number")
  private BigDecimal price;

  @NotNull(message = "Quantity cannot be empty")
  @Positive(message = "Quantity must be a positive number")
  private long quantity;
}
