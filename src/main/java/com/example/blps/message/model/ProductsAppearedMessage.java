package com.example.blps.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class ProductsAppearedMessage extends AbstractMessage implements Serializable{
    private String username;
    private long orderId;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductsAppearedMessage)) return false;
        ProductsAppearedMessage that = (ProductsAppearedMessage) o;
        return getOrderId() == that.getOrderId() && getUsername().equals(that.getUsername()) && getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getOrderId(), getEmail());
    }
}
