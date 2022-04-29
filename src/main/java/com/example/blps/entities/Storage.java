package com.example.blps.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name="storage")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name="count")
    private long count;
}
