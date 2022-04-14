package com.example.blps.repositories;

import com.example.blps.entities.Category;
import com.example.blps.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(Category category);
    List<Product> findAllByPriceBetween(float min, float max);
}
