package com.suinfinity.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDTO {

  @NotNull(message = "Product id cannot be empty")
  @Positive(message = "Product id must be a positive number")
  private long productId;

  @NotNull(message = "Quantity cannot be empty")
  @Positive(message = "Quantity must be a positive number")
  private long quantity;
}
