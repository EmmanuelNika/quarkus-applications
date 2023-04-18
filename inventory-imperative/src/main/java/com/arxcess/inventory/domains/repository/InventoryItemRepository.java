package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import java.util.Optional;

@ApplicationScoped
public class InventoryItemRepository implements PanacheRepository<InventoryItem> {

    public InventoryItem getById(Long id) {
        return findByIdOptional(id).orElseThrow(() -> new NotFoundException(String.format("Item %d not found", id)));
    }

    public Optional<InventoryItem> validateRequest(String name, String barcode) {

        return find("name = ?1 or barcode = ?2", name, barcode).singleResultOptional();

    }

    public Optional<InventoryItem> validateRequest(Long id, String name, String barcode) {

        return find("id <> ?1 and (name = ?2 or barcode = ?3)", id, name, barcode).singleResultOptional();

    }

}
