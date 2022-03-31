package com.example.blps.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity(name="orders")
@Table(name="orders")
public class Order {
    @Id
    @Column(name="order_id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
