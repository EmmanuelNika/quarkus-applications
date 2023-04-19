package com.arxcess.inventory.controllers.services;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.RowSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@ApplicationScoped
public class InventoryCommonService {

    @Inject
    MySQLPool client;

    public Uni<BigDecimal> getQuantityOfItem(Long id) {

        String query = """
                SELECT
                SUM(quantity) AS quantity
                FROM InventoryActivity
                WHERE inventoryItem_id = %d""".formatted(id);

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal("quantity") : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

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
