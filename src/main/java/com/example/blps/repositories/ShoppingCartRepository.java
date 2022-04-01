package com.example.blps.repositories;

import com.example.blps.entities.ShoppingCart;
import com.example.blps.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    public List<ShoppingCart> findAllByKeyUserAndConfirmed(User user, boolean conf);
}
