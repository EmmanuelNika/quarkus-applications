package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryActivity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class InventoryActivityRepository implements PanacheRepository<InventoryActivity> {

    public InventoryActivity getById(Long id) {
        return findByIdOptional(id).orElseThrow(() -> new NotFoundException(String.format("Activity %d not found", id)));
    }

}
