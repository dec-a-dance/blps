package com.example.blps.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Data
@Entity(name="shopping_cart")
@Table(name="shopping_cart")
public class ShoppingCart {
    @Embeddable
    public static class ShoppingCartKey implements Serializable {
        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", referencedColumnName = "product_id")
        @Getter
        @Setter
        private Product product;

        @NotNull
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username", referencedColumnName = "username")
        @Getter
        @Setter
        private User user;
    }

    @EmbeddedId
    private ShoppingCartKey key;

    private long count;

    private boolean confirmed;
}
