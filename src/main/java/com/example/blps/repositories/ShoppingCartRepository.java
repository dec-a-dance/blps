package com.example.blps.repositories;

import com.example.blps.entities.Product;
import com.example.blps.entities.ShoppingCart;
import com.example.blps.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    public List<ShoppingCart> findAllByKeyUserAndConfirmed(User user, boolean conf);
    public int deleteByKey_UserAndKey_ProductAndConfirmed(User user, Product product, boolean conf);
    ShoppingCart findByKey_UserAndKey_ProductAndConfirmed(User user, Product product, boolean conf);
}
