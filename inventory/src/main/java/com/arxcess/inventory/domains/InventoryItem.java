package com.arxcess.inventory.domains;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class InventoryItem extends PanacheEntity {

    public String name;

    public String barcode;

    public String type;

    public Boolean isReturnable;

}
