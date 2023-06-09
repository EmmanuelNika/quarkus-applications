package com.arxcess.inventory.domains;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class InventoryItem extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String name;

    public String barcode;

    @Column(nullable = false)
    public String type;

    public Boolean isReturnable;

}
