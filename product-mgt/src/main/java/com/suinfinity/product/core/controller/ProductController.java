package com.suinfinity.product.core.controller;

import com.suinfinity.product.core.dto.ProductDTO;
import com.suinfinity.product.core.dto.ProductResponseDTO;
import com.suinfinity.product.core.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping(value = "/get/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<ProductResponseDTO> getProduct(@NotBlank @PathVariable String name) {
    return productService.getProduct(name);
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<ProductResponseDTO>> getProducts() {
    return productService.getProducts();
  }

  @PutMapping(value = "/update/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<ProductResponseDTO> updateProduct(
      @NotBlank @PathVariable String name, @Valid @RequestBody ProductDTO productDTO) {
    return productService.updateProduct(name, productDTO);
  }

  @DeleteMapping(value = "/delete/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<?> deleteProduct(@NotBlank @PathVariable String name) {
    return productService.deleteProduct(name);
  }
}
