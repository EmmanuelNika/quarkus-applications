package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryActivity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryActivityRepository implements PanacheRepository<InventoryActivity> {

}
