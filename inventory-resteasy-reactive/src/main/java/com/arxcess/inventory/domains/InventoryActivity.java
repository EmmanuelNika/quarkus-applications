package com.arxcess.inventory.domains;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class InventoryActivity extends PanacheEntity {

    @JsonbDateFormat(value = "dd/MM/yyyy HH:mm")
    @Column(nullable = false)
    public LocalDateTime date;

    @Column(nullable = false)
    public String transaction;

    @Column(nullable = false, scale = 6)
    public BigDecimal quantity;

    @Column(scale = 6)
    public BigDecimal quantityRemaining;

    @Column(nullable = false, scale = 6)
    public BigDecimal unitPrice;

    @Column(nullable = false, scale = 6)
    public BigDecimal costPrice;

    @Column(scale = 6)
    public BigDecimal sellingPrice;

    @ManyToOne
    @JoinColumn(nullable = false)
    public InventoryItem inventoryItem;

    @ManyToOne
    @JoinColumn
    public InventoryItemSerialNumber inventoryItemSerialNumber;

    @ManyToOne
    @JoinColumn
    public BatchInfo batchInfo;

    @ManyToOne
    @JoinColumn
    @JsonbTransient
    public InventoryActivity activityLine;

}
