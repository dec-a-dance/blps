package com.example.blps.repositories;

import com.example.blps.entities.Order;
import com.example.blps.entities.OrderStatus;
import com.example.blps.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findById(long id);
    public List<Order> findAllByUser(User user);
    List<Order> findAllByStatus(OrderStatus status);
}
