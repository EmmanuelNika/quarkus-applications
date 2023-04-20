package com.arxcess.inventory.domains;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbTransient;
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

    @JsonbDateFormat(value = "dd/MM/yyyy")
    public LocalDate manufacturingDate;

    @JsonbDateFormat(value = "dd/MM/yyyy")
    public LocalDate expiryDate;

    @ManyToOne
    @JsonbTransient
    @JoinColumn(nullable = false)
    public InventoryItem inventoryItem;

}
