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
                (SELECT unitPrice FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining > 0 ORDER BY DATE(date) ASC LIMIT %d),
                (SELECT unitPrice FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining = 0 ORDER BY DATE(date) DESC LIMIT %d)
                ) AS  fifoCost
            """;
    public static final String FEFO_PRICE_QUERY = """
            SELECT
                IF(
                (SELECT SUM(a.quantityRemaining) FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d) > 0,
                (SELECT a.unitPrice FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d AND a.quantityRemaining > 0 ORDER BY DATE(b.expiryDate) ASC LIMIT %d),
                (SELECT a.unitPrice FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d AND a.quantityRemaining = 0 ORDER BY DATE(b.expiryDate) DESC LIMIT %d)
                ) AS  fefoCost
            """;

    private CostQuery() {
    }
}
