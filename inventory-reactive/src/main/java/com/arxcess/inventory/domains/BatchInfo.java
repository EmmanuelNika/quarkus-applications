package com.arxcess.inventory.domains;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class BatchInfo extends PanacheEntity {

    @Column(nullable = false)
    public String batchReference;

    @Column(nullable = false)
    public String batchNumber;

    public LocalDate manufacturingDate;

    public LocalDate expiryDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    public InventoryItem inventoryItem;

}
