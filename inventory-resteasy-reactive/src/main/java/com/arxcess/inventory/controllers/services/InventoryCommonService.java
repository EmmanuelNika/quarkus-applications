package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.controllers.services.statics.CostQuery;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.RowSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

@ApplicationScoped
public class InventoryCommonService {

    private static final String QTY = "quantity";

    private static final String AVG_PRICE = "averageCost";

    private static final String FEFO_PRICE ="fefoCost";

    private static final String FIFO_PRICE ="fifoCost";

    private static final String HIFO_PRICE ="hifoCost";

    private static final String LIFO_PRICE ="lifoCost";

    private static final String MARK_UP = "markUp";

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
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(QTY) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

    public Uni<BigDecimal> calculateAverageCost(Long id) {

        String query = CostQuery.AVG_PRICE_QUERY.formatted(id, LocalDate.now(), id, LocalDate.now(), id, LocalDate.now());

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(AVG_PRICE) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

    public Uni<BigDecimal> calculateAverageCost(Long id, LocalDate localDate) {

        String query = CostQuery.AVG_PRICE_QUERY.formatted(id, localDate, id, localDate, id, localDate);

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(AVG_PRICE) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

    public Uni<BigDecimal> calculateFEFOCost(Long id) {

        String query = CostQuery.FEFO_PRICE_QUERY.formatted(id, id, id);

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(FEFO_PRICE) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

    public Uni<BigDecimal> calculateFIFOCost(Long id) {

        String query = CostQuery.FIFO_PRICE_QUERY.formatted(id, id, id);

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(FIFO_PRICE) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

    public Uni<BigDecimal> calculateHIFOCost(Long id) {

        String query = CostQuery.HIFO_PRICE_QUERY.formatted(id, id, id);

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(HIFO_PRICE) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

    public Uni<BigDecimal> calculateLIFOCost(Long id) {

        String query = CostQuery.LIFO_PRICE_QUERY.formatted(id, id, id);

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(LIFO_PRICE) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);

    }

    public Uni<BigDecimal> calculateSellingPriceFromAverageCost(Long id) {
        String query = CostQuery.AVG_PRICE_QUERY.concat(", (SELECT markUp FROM InventoryItem WHERE id = %d) AS markUp")
                .formatted(id, LocalDate.now(), id, LocalDate.now(), id, LocalDate.now(), id);

        return client.preparedQuery(query).execute().onItem().transform(rowSet -> {
            BigDecimal averageCost = rowSet.iterator().next().getBigDecimal(AVG_PRICE) != null ? rowSet.iterator().next().getBigDecimal(AVG_PRICE) : BigDecimal.ZERO;
            BigDecimal markUp = rowSet.iterator().next().getBigDecimal(MARK_UP) != null ? rowSet.iterator().next().getBigDecimal(MARK_UP) : BigDecimal.ZERO;

            return averageCost.add((averageCost.multiply(markUp.multiply(BigDecimal.valueOf(0.01)))));

        });

    }

    public Uni<BigDecimal> getAvailableBySerialNumber(Long id) {
        String query = """
                SELECT id
                SUM(quantity) AS quantity
                FROM InventoryActivity
                WHERE inventoryItemSerialNumber = %d
                """.formatted(id);

        return client.preparedQuery(query).execute()
                .onItem().ifNotNull().transform(RowSet::iterator)
                .onItem().ifNotNull().transform(iterator -> iterator.hasNext() ? iterator.next().getBigDecimal(QTY) : BigDecimal.ZERO)
                .onItem().ifNull().continueWith(() -> BigDecimal.ZERO);
    }
}
