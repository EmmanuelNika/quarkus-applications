package com.arxcess.inventory.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

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

    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate manufacturingDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate expiryDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false)
    public InventoryItem inventoryItem;

}
