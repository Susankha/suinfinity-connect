package com.suinfinity.product.core.service;

import com.suinfinity.product.core.dto.ProductDTO;
import com.suinfinity.product.core.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ProductService {

  @Autowired private ProductRepository productRepository;

  public ResponseEntity<?> registerProduct(ProductDTO productDTO) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body("product " + productDTO.getProductName() + " successfully registered");
  }
}
