package com.arxcess.inventory.controllers.services;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@ApplicationScoped
public class InventoryCostService {

    @Inject
    MySQLPool client;

    public Uni<BigDecimal> calculateAverageCost(Long id) {
        String query = """
                SELECT
                SUM(costPrice) AS costPrice,
                SUM(quantity) AS quantity
                FROM InventoryActivity
                WHERE inventoryItem_id = %d
                AND DATE(date) <= '%s'""".formatted(id, LocalDate.now());

        return client.preparedQuery(query).execute().onItem().transform(rowSet -> {
            BigDecimal cost = rowSet.iterator().next().getBigDecimal("costPrice");
            BigDecimal quantity = rowSet.iterator().next().getBigDecimal("quantity");

            return cost.divide(quantity, 6, RoundingMode.HALF_EVEN);

        });
    }

    public Uni<BigDecimal> calculateAverageCost(Long id, LocalDate localDate) {
        String query = """
                SELECT
                SUM(costPrice) AS costPrice,
                SUM(quantity) AS quantity
                FROM InventoryActivity
                WHERE inventoryItem_id = %d
                AND DATE(date) <= '%s'""".formatted(id, localDate);

        return client.preparedQuery(query).execute().onItem().transform(rowSet -> {
            BigDecimal cost = rowSet.iterator().next().getBigDecimal("costPrice");
            BigDecimal quantity = rowSet.iterator().next().getBigDecimal("quantity");

            return cost.divide(quantity, 6, RoundingMode.HALF_EVEN);

        });
    }
}
