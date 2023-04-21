package com.arxcess.inventory.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
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
    @JsonIgnore
    public InventoryItemSerialNumber inventoryItemSerialNumber;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    public BatchInfo batchInfo;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    public InventoryActivity activityLine;

}
