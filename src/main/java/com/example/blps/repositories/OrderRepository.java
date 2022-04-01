package com.example.blps.repositories;

import com.example.blps.entities.Order;
import com.example.blps.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findAllByUser(User user);
}
