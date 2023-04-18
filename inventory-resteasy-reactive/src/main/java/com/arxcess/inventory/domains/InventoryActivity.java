package com.arxcess.inventory.domains;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class InventoryActivity extends PanacheEntity {

    @Column(nullable = false)
    public LocalDateTime date;

    @Column(nullable = false)
    public String transaction;

    @Column(nullable = false, scale = 6)
    public BigDecimal quantity;

    @Column(nullable = false, scale = 6)
    public BigDecimal costPrice;

    @Column(scale = 6)
    public BigDecimal sellingPrice;

    @ManyToOne
    @JoinColumn(nullable = false)
    public InventoryItem inventoryItem;

}
