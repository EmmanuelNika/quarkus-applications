package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.InventoryItemSerialNumber;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class InventoryItemSerialNumberRepository implements PanacheRepository<InventoryItemSerialNumber> {

    public Optional<InventoryItemSerialNumber> validateRequest(String serialNumber) {

        return find("serialNumber = ?1", serialNumber).singleResultOptional();

    }

    public Boolean hasSerialNumber(InventoryItem inventoryItem) {

        return find("inventoryItem = ?1", inventoryItem).firstResultOptional().isPresent();

    }

}
