package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryItem;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryItemRepository implements PanacheRepository<InventoryItem> {

    public Uni<InventoryItem> validateRequest(String name, String barcode) {

        return list("name = ?1 or barcode = ?2", name, barcode).onItem()
                .transform(inventoryItems -> {

                    if (inventoryItems.isEmpty()) {
                        return null;

                    } else {
                        return inventoryItems.get(0);

                    }
                });
    }

    public Uni<InventoryItem> validateRequest(Long id, String name, String barcode) {

        return list("id <> ?1 and (name = ?2 or barcode = ?3)", id, name, barcode).onItem()
                .transform(inventoryItems -> {

                    if (inventoryItems.isEmpty()) {
                        return null;

                    } else {
                        return inventoryItems.get(0);

                    }
                });
    }

}
