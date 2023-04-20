package com.arxcess.inventory.domains;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class InventoryItemSerialNumber extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String serialNumber;

    @ManyToOne
    @JsonbTransient
    @JoinColumn(nullable = false)
    public InventoryItem inventoryItem;

}
