package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryActivity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

@ApplicationScoped
public class InventoryActivityRepository implements PanacheRepository<InventoryActivity> {

    public InventoryActivity getById(Long id) {
        return findByIdOptional(id).orElseThrow(() -> new NotFoundException(String.format("Activity %d not found", id)));
    }

    public InventoryActivity getFirstInForSaleActivity(Long itemId) {
        return find("inventoryItem_id = ?1 AND batchInfo IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0 ORDER BY date ASC", itemId).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

    public InventoryActivity getLastInForSaleActivity(Long itemId) {
        return find("inventoryItem_id = ?1 AND batchInfo IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0 ORDER BY date DESC", itemId).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

    public InventoryActivity getHighestInForSaleActivity(Long itemId) {
        return find("inventoryItem_id = ?1 AND MAX(unitPrice) AND batchInfo IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0", itemId).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

    public InventoryActivity getSerialNoForSaleActivity(Long itemId, String serialNumber) {
        return find("inventoryItem_id = ?1 AND inventoryItemSerialNumber.serialNumber = ?2 AND batchInfo IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0", itemId, serialNumber).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

    public InventoryActivity getFirstInBatchForSaleActivity(Long itemId, String batchNumber) {
        return find("inventoryItem_id = ?1 AND batchInfo.batchNumber IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0 ORDER BY date ASC", itemId).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

    public InventoryActivity getLastInBatchForSaleActivity(Long itemId, String batchNumber) {
        return find("inventoryItem_id = ?1 AND batchInfo.batchNumber IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0 ORDER BY date DESC", itemId).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

    public InventoryActivity getHighestInBatchForSaleActivity(Long itemId, String batchNumber) {
        return find("inventoryItem_id = ?1 AND MAX(unitPrice) AND batchInfo.batchNumber IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0", itemId).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

    public InventoryActivity getFirstExpiryBatchForSaleActivity(Long itemId, String batchNumber) {
        return find("inventoryItem_id = ?1 AND batchInfo.batchNumber IS NULL AND inventoryItemSerialNumber IS NULL AND quantityRemaining > 0 ORDER BY batchInfo.expiryDate DESC", itemId).firstResultOptional().orElseThrow(() -> new WebApplicationException("No items available for sale"));
    }

}
