package com.example.blps.entities;

import lombok.Data;

import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
@Entity(name="order_product")
@Table(name="order_product")
public class OrderProduct {
    @Embeddable
    public static class OrderProductKey implements Serializable {
        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn (name = "product_id", referencedColumnName = "product_id")
        private Product product;

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn (name = "order_id", referencedColumnName = "order_id")
        private Order order;
    }

    @EmbeddedId
    private OrderProductKey key;

    private long count;
}
