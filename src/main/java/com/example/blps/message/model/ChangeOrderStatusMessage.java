package com.example.blps.message.model;

import com.example.blps.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class ChangeOrderStatusMessage extends AbstractMessage implements Serializable {
    private String username;
    private long orderId;
    private String email;
    private OrderStatus newStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeOrderStatusMessage)) return false;
        ChangeOrderStatusMessage that = (ChangeOrderStatusMessage) o;
        return getOrderId() == that.getOrderId() && getUsername().equals(that.getUsername()) && getEmail().equals(that.getEmail()) && getNewStatus() == that.getNewStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getOrderId(), getEmail(), getNewStatus());
    }
}
