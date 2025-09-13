package com.suinfinity.product.service;

import com.suinfinity.product.dto.ProductDTO;
import com.suinfinity.product.dto.ProductResponseDTO;
import com.suinfinity.product.exception.ProductNotFoundException;
import com.suinfinity.product.mapper.ProductMapper;
import com.suinfinity.product.model.Product;
import com.suinfinity.product.repository.ProductRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    Product product =
        productRepository
            .findByName(productName)
            .orElseThrow(
                () -> {
                  log.error("Product '{}' does not exist ", productName);
                  return new ProductNotFoundException(
                      "Product " + "'" + productName + "'" + " does not exist");
                });

    ProductResponseDTO productResponseDTO = ProductMapper.INSTANCE.toProductResponseDTO(product);
    return ResponseEntity.ok().body(productResponseDTO);
  }

  public ResponseEntity<List<ProductResponseDTO>> getProducts() {
    List<Product> products = productRepository.findAll();
    List<ProductResponseDTO> productResponseDTOS =
        products.stream().map(ProductMapper.INSTANCE::toProductResponseDTO).toList();
    return ResponseEntity.ok().body(productResponseDTOS);
  }

  public ResponseEntity<ProductResponseDTO> updateProduct(
      String productName, ProductDTO productDTO) {
    Product product =
        productRepository
            .findByName(productName)
            .orElseThrow(
                () -> {
                  log.error("updating failed, product '{}' does not exist", productName);
                  return new ProductNotFoundException(
                      "Product updating failed, " + "'" + productName + "'" + " does not exist");
                });

    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setStockQuantity(productDTO.getStockQuantity());
    try {
      productRepository.save(product);
    } catch (RuntimeException ex) {
      throw new RuntimeException("Product " + "'" + productName + "'" + " update failed");
    }
    ProductResponseDTO productResponseDTO = ProductMapper.INSTANCE.toProductResponseDTO(product);
    return ResponseEntity.ok().body(productResponseDTO);
  }

  @Transactional
  public ResponseEntity<Void> deleteProduct(String name) {
    long deletedCount = productRepository.deleteByName(name);
    if (deletedCount == 0) {
      log.error("Deleting failed, Product '{}' does not exist", name);
      throw new ProductNotFoundException(
          "Product deleting failed, " + "'" + name + "'" + " does not exist");
    }
    return ResponseEntity.noContent().build();
  }
}
