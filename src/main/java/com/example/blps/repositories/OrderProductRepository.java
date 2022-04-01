package com.example.blps.repositories;

import com.example.blps.entities.Order;
import com.example.blps.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    public List<OrderProduct> findAllByKey_Order(Order order);
}
