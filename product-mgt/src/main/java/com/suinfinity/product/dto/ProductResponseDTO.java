package com.suinfinity.product.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductResponseDTO {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private long stockQuantity;
}
