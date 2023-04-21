package com.arxcess.inventory.domains;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class InventoryItem extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String name;

    public String barcode;

    @Column(nullable = false)
    public String type;

    public Boolean isReturnable;

    public BigDecimal markUp;

}
