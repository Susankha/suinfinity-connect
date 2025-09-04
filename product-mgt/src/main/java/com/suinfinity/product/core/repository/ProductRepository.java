package com.suinfinity.product.core.repository;

import com.suinfinity.product.core.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Product findByName(String name);

  long deleteByName(String name);
}
