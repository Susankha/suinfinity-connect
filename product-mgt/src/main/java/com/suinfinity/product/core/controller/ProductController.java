package com.suinfinity.product.core.controller;

import com.suinfinity.product.core.dto.ProductDTO;
import com.suinfinity.product.core.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired private ProductService productService;

  @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<?> registerProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.registerProduct(productDTO);
  }
}
