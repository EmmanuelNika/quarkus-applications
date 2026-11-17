package com.arxcess.inventory.controllers.services.statics;

public class CostQuery {

    public static final String MARK_UP_QUERY = ", (SELECT markUp FROM InventoryItem WHERE id = %d) AS markUp";

    public static final String AVG_PRICE_QUERY = """
                                                 SELECT
                                                     IF(
                                                     (SELECT SUM(quantity) FROM InventoryActivity WHERE inventoryItem_id = %d AND DATE(date) <= '%s') > 0,
                                                     (SELECT SUM(lineCost) / SUM(quantity) FROM InventoryActivity WHERE inventoryItem_id = %d AND DATE(date) <= '%s'),
                                                     (SELECT unitCost FROM InventoryActivity WHERE inventoryItem_id = %d AND DATE(date) <= '%s' ORDER BY DATE(date) DESC LIMIT 1)
                                                     ) AS averageCost
                                                 """;

    public static final String FIFO_PRICE_QUERY = """
                                                  SELECT
                                                      IF(
                                                      (SELECT SUM(quantityRemaining) FROM InventoryActivity WHERE inventoryItem_id = %d) > 0,
                                                      (SELECT unitCost FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining > 0 ORDER BY UNIX_TIMESTAMP(date) ASC LIMIT 1),
                                                      (SELECT unitCost FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining = 0 ORDER BY UNIX_TIMESTAMP(date) DESC LIMIT 1)
                                                      ) AS  fifoCost
                                                  """;

    public static final String FEFO_PRICE_QUERY = """
                                                  SELECT
                                                      IF(
                                                      (SELECT SUM(a.quantityRemaining) FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d) > 0,
                                                      (SELECT a.unitCost FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d AND a.quantityRemaining > 0 ORDER BY DATE(b.expiryDate) ASC LIMIT 1),
                                                      (SELECT a.unitCost FROM InventoryActivity a INNER JOIN BatchInfo b ON a.batchInfo_id = b.id WHERE a.inventoryItem_id = %d AND a.quantityRemaining = 0 ORDER BY DATE(b.expiryDate) DESC LIMIT 1)
                                                      ) AS  fefoCost
                                                  """;

    public static final String HIFO_PRICE_QUERY = """
                                                  SELECT
                                                      IF(
                                                      (SELECT SUM(quantityRemaining) FROM InventoryActivity WHERE inventoryItem_id = %d) > 0,
                                                      (SELECT MAX(unitCost) FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining > 0),
                                                      (SELECT MIN(unitCost) FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining = 0)
                                                      ) AS  hifoCost
                                                  """;

    public static final String LIFO_PRICE_QUERY = """
                                                  SELECT
                                                      IF(
                                                      (SELECT SUM(quantityRemaining) FROM InventoryActivity WHERE inventoryItem_id = %d) > 0,
                                                      (SELECT unitCost FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining > 0 ORDER BY UNIX_TIMESTAMP(date) DESC LIMIT 1),
                                                      (SELECT unitCost FROM InventoryActivity WHERE inventoryItem_id = %d AND quantityRemaining = 0 ORDER BY UNIX_TIMESTAMP(date) ASC LIMIT 1)
                                                      ) AS  lifoCost
                                                  """;

    private CostQuery() {
    }
}
