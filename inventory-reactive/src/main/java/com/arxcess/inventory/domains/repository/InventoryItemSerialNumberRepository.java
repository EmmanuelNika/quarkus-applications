package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryItemSerialNumber;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryItemSerialNumberRepository implements PanacheRepository<InventoryItemSerialNumber> {

    public Uni<InventoryItemSerialNumber> validateRequest(String serialNumber) {

        return list("serialNumber = ?1", serialNumber).onItem().transform(itemSerialNumbers -> {
            if (itemSerialNumbers.isEmpty()) {
                return null;

            } else {
                return itemSerialNumbers.get(0);

            }
        });
    }

}
