package com.example.blps.repositories;

import com.example.blps.entities.Product;
import com.example.blps.entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Optional<Storage> findByProduct(Product product);
}
