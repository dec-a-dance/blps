package com.example.blps.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.EnumType.STRING;

@Data
@Entity(name="orders")
@Table(name="orders")
public class Order {
    @Id
    @Column(name="order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(STRING)
    @Column(nullable = false, name="status")
    private OrderStatus status;
}
