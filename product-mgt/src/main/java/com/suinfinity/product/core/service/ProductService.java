package com.suinfinity.product.core.service;

import com.suinfinity.product.core.dto.ProductDTO;
import com.suinfinity.product.core.dto.ProductResponseDTO;
import com.suinfinity.product.core.mapper.ProductMapper;
import com.suinfinity.product.core.model.Product;
import com.suinfinity.product.core.repository.ProductRepository;
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
    Product product = productRepository.findByName(productName);
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
    Product product = productRepository.findByName(productName);
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setStockQuantity(productDTO.getStockQuantity());

    productRepository.save(product);
    ProductResponseDTO productResponseDTO = ProductMapper.INSTANCE.toProductResponseDTO(product);

    return ResponseEntity.ok().body(productResponseDTO);
  }

  @Transactional
  public ResponseEntity<Void> deleteProduct(String name) {
    productRepository.deleteByName(name);
    return ResponseEntity.noContent().build();
  }
}
