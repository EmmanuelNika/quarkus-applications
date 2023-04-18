package com.arxcess.inventory.domains;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InventoryItemSerialNumber extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String serialNumber;

    @ManyToOne
    @JoinColumn(nullable = false)
    public InventoryItem inventoryItem;

}
