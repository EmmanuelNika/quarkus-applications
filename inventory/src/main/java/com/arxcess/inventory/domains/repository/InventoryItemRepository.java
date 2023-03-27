package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryItem;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryItemRepository implements PanacheRepository<InventoryItem> {

}
