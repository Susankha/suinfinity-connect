package com.suinfinity.product.core.service;

import com.suinfinity.product.core.dto.ProductDTO;
import com.suinfinity.product.core.dto.ProductResponseDTO;
import com.suinfinity.product.core.mapper.ProductMapper;
import com.suinfinity.product.core.model.Product;
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

  public ResponseEntity<?> registerProduct(ProductDTO productDTO) throws RuntimeException {
    Product product = ProductMapper.INSTANCE.toProduct(productDTO);
    try {
      productRepository.save(product);
    } catch (RuntimeException ex) {
      log.error("product {} registration failed ", product.getName());
      throw new RuntimeException(
          "product " + "'" + product.getName() + "'" + " registration failed");
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("product " + productDTO.getName() + " successfully registered");
  }

  public ResponseEntity<ProductResponseDTO> getProduct(String productName) {
    Product product = productRepository.findByName(productName);
    ProductResponseDTO productResponseDTO = ProductMapper.INSTANCE.toProductResponseDTO(product);

    return ResponseEntity.ok().body(productResponseDTO);
  }
}
