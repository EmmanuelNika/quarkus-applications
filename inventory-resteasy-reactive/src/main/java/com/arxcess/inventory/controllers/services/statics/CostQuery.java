package com.arxcess.inventory.controllers.services.statics;

public class CostQuery {

    public static final String AVG_PRICE_QUERY = """
            SELECT
                IF(
                (SELECT SUM(quantity) FROM InventoryActivity WHERE inventoryItem_id = %d AND DATE(date) <= '%s') > 0,
                (SELECT SUM(costPrice) / SUM(quantity) FROM InventoryActivity WHERE inventoryItem_id = %d AND DATE(date) <= '%s'),
                (SELECT ABS(unitPrice) FROM InventoryActivity WHERE inventoryItem_id = %d AND DATE(date) <= '%s' ORDER BY DATE(date) DESC LIMIT 1)
                ) AS averageCost
            """;

    public static final String FIFO_PRICE_QUERY = """
            SELECT
                IF(
                (SELECT SUM(quantityRemaining) FROM InventoryActivity WHERE inventoryItem_id = %d) > 0,
                (SELECT unitPrice FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining > 0 ORDER BY date ASC LIMIT 1),
                (SELECT unitPrice FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining = 0 ORDER BY date DESC LIMIT 1)
                ) AS  fifoCost
            """;

    public static final String FEFO_PRICE_QUERY = """
            SELECT
                IF(
                (SELECT SUM(a.quantityRemaining) FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d) > 0,
                (SELECT a.unitPrice FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d AND a.quantityRemaining > 0 ORDER BY DATE(b.expiryDate) ASC LIMIT 1),
                (SELECT a.unitPrice FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d AND a.quantityRemaining = 0 ORDER BY DATE(b.expiryDate) DESC LIMIT 1)
                ) AS  fefoCost
            """;

    public static final String HIFO_PRICE_QUERY = """
            SELECT
                IF(
                (SELECT SUM(quantityRemaining) FROM InventoryActivity WHERE inventoryItem_id = %d) > 0,
                (SELECT MAX(unitPrice) FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining > 0),
                (SELECT MIN(unitPrice) FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining = 0)
                ) AS  hifoCost
            """;

    public static final String LIFO_PRICE_QUERY = """
            SELECT
                IF(
                (SELECT SUM(quantityRemaining) FROM InventoryActivity WHERE inventoryItem_id = %d) > 0,
                (SELECT unitPrice FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining > 0 ORDER BY date DESC LIMIT 1),
                (SELECT unitPrice FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining = 0 ORDER BY date ASC LIMIT 1)
                ) AS  lifoCost
            """;

    private CostQuery() {
    }
}
