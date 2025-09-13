package com.suinfinity.product.repository;

import com.suinfinity.product.model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByName(String name);

  long deleteByName(String name);
}
