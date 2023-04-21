package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.controllers.endpoints.TransactionController;
import com.arxcess.inventory.controllers.services.payloads.ReceiveRequest;
import com.arxcess.inventory.controllers.services.payloads.SaleItemRequest;
import com.arxcess.inventory.controllers.services.payloads.SaleRequest;
import com.arxcess.inventory.controllers.services.statics.ActivityTransactionTypes;
import com.arxcess.inventory.controllers.services.statics.PaymentTypes;
import com.arxcess.inventory.domains.BatchInfo;
import com.arxcess.inventory.domains.InventoryActivity;
import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.InventoryItemSerialNumber;
import com.arxcess.inventory.domains.repository.BatchInfoRepository;
import com.arxcess.inventory.domains.repository.InventoryActivityRepository;
import com.arxcess.inventory.domains.repository.InventoryItemRepository;
import com.arxcess.inventory.domains.repository.InventoryItemSerialNumberRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TransactionService {

    private static final String METHOD_NOT_FOUND = "Method type not found!";

    private static final String NO_ITEMS_FOUND = "No items available for sale";

    @Inject
    InventoryItemRepository itemRepository;

    @Inject
    InventoryItemSerialNumberRepository serialNumberRepository;

    @Inject
    BatchInfoRepository batchInfoRepository;

    @Inject
    InventoryActivityRepository activityRepository;

    @Inject
    InventoryCommonService inventoryCommonService;

    public Response createReceive(ReceiveRequest request) {

        List<InventoryActivity> activities = new ArrayList<>();

        request.itemRequests.forEach(receiveItemRequest -> {
            InventoryItem inventoryItem = itemRepository.getById(receiveItemRequest.itemId);

            if (Boolean.TRUE.equals(receiveItemRequest.serialNumbers.isEmpty()) && receiveItemRequest.batchInfo == null) {
                InventoryActivity inventoryActivity = new InventoryActivity();
                inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                inventoryActivity.date = request.date.atTime(LocalTime.now());
                inventoryActivity.unitPrice = receiveItemRequest.costPrice;
                inventoryActivity.costPrice = receiveItemRequest.costPrice.multiply(receiveItemRequest.quantity);
                inventoryActivity.quantity = receiveItemRequest.quantity;
                inventoryActivity.quantityRemaining = receiveItemRequest.quantity;
                inventoryActivity.inventoryItem = inventoryItem;

                inventoryActivity.persist();

                activities.add(inventoryActivity);

            } else if (Boolean.FALSE.equals(receiveItemRequest.serialNumbers.isEmpty()) && receiveItemRequest.batchInfo == null) {

                receiveItemRequest.serialNumbers.forEach(serialNumber -> {
                    InventoryActivity inventoryActivity = new InventoryActivity();
                    inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                    inventoryActivity.date = request.date.atTime(LocalTime.now());
                    inventoryActivity.unitPrice = receiveItemRequest.costPrice;
                    inventoryActivity.costPrice = receiveItemRequest.costPrice;
                    inventoryActivity.quantity = BigDecimal.valueOf(1);
                    inventoryActivity.quantityRemaining = BigDecimal.valueOf(1);
                    inventoryActivity.inventoryItem = inventoryItem;

                    InventoryItemSerialNumber itemSerialNumber = new InventoryItemSerialNumber();
                    itemSerialNumber.serialNumber = serialNumber;
                    itemSerialNumber.inventoryItem = inventoryItem;
                    itemSerialNumber.persist();

                    inventoryActivity.inventoryItemSerialNumber = itemSerialNumber;
                    inventoryActivity.persist();

                    activities.add(inventoryActivity);

                });

            } else {

                InventoryActivity inventoryActivity = new InventoryActivity();
                inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                inventoryActivity.date = request.date.atTime(LocalTime.now());
                inventoryActivity.unitPrice = receiveItemRequest.costPrice;
                inventoryActivity.costPrice = receiveItemRequest.costPrice;
                inventoryActivity.quantity = receiveItemRequest.quantity;
                inventoryActivity.quantityRemaining = receiveItemRequest.quantity;
                inventoryActivity.inventoryItem = inventoryItem;

                BatchInfo batchInfo = new BatchInfo();
                batchInfo.batchReference = batchInfoRepository.generateBatchReference(request.date);
                batchInfo.batchNumber = receiveItemRequest.batchInfo.batchNumber;
                batchInfo.manufacturingDate = receiveItemRequest.batchInfo.manufacturingDate;
                batchInfo.expiryDate = receiveItemRequest.batchInfo.expiryDate;
                batchInfo.inventoryItem = inventoryItem;
                batchInfo.persist();

                inventoryActivity.batchInfo = batchInfo;
                inventoryActivity.persist();

                activities.add(inventoryActivity);
            }
        });

        return Response.created(UriBuilder.fromResource(TransactionController.class).path("").build())
                .entity(activities).build();

    }

    public Response makeSale(SaleRequest request) {
        List<InventoryActivity> activities = new ArrayList<>();

        for (SaleItemRequest saleItemRequest : request.itemRequests) {
            InventoryItem inventoryItem = itemRepository.getById(saleItemRequest.itemId);

            if (Boolean.FALSE.equals(saleItemRequest.serialNumbers.isEmpty()) && Boolean.TRUE.equals(serialNumberRepository.hasSerialNumber(inventoryItem))) {
                // FIFO, HIFO, LIFO
                activities.addAll(saleItemWithSerialNumber(inventoryItem, saleItemRequest, request.date));

            } else if (saleItemRequest.batchNumber != null && Boolean.TRUE.equals(batchInfoRepository.hasBatchNumber(inventoryItem))) {
                // FIFO, HIFO, LIFO, FEFO
                activities.add(saleItemWithBatchInfo(inventoryItem, saleItemRequest, request.date, request.method));

            } else {
                // AVG, FIFO, HIFO, LIFO
                activities.addAll(saleUnTrackedItem(inventoryItem, saleItemRequest, request.date, request.method));
            }

        }

        return Response.created(UriBuilder.fromResource(TransactionController.class).path("").build())
                .entity(activities).build();
    }

    private List<InventoryActivity> saleUnTrackedItem(InventoryItem inventoryItem, SaleItemRequest saleItemRequest, LocalDate saleDate, String method) {

        List<InventoryActivity> activities = new ArrayList<>();

        BigDecimal qty = inventoryCommonService.getQuantityOfUntrackedItem(inventoryItem.id).onItem()
                .transform(BigDecimal::abs).await().indefinitely();

        if (qty.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal qtyToBeSold = saleItemRequest.quantity;

            while (qtyToBeSold.compareTo(BigDecimal.ZERO) > 0) {
                InventoryActivity activityLine = getActivityLine(inventoryItem.id, method);

                BigDecimal availableQty = activityLine.quantityRemaining;
                BigDecimal soldQty = availableQty.subtract(qtyToBeSold)
                        .compareTo(BigDecimal.ZERO) < 0 ? availableQty : qtyToBeSold;

                BigDecimal unitPrice = getCostPrice(inventoryItem.id, method);
                BigDecimal sellingPrice = getSellingPrice(inventoryItem.id, method);

                InventoryActivity inventoryActivity = new InventoryActivity();
                inventoryActivity.transaction = ActivityTransactionTypes.SALE;
                inventoryActivity.date = saleDate.atTime(LocalTime.now());
                inventoryActivity.unitPrice = unitPrice.negate();
                inventoryActivity.sellingPrice = sellingPrice;
                inventoryActivity.costPrice = unitPrice.multiply(soldQty).negate();
                inventoryActivity.quantity = soldQty.negate();
                inventoryActivity.inventoryItem = inventoryItem;
                inventoryActivity.activityLine = activityLine;

                inventoryActivity.persist();

                activityLine.quantityRemaining = activityLine.quantityRemaining.subtract(soldQty);
                activityLine.persist();

                qtyToBeSold = qtyToBeSold.subtract(soldQty);

                activities.add(inventoryActivity);

            }

            return activities;

        } else {

            throw new WebApplicationException(NO_ITEMS_FOUND);

        }
    }

    private List<InventoryActivity> saleItemWithSerialNumber(InventoryItem inventoryItem, SaleItemRequest saleItemRequest, LocalDate saleDate) {
        List<InventoryActivity> activities = new ArrayList<>();

        saleItemRequest.serialNumbers.forEach(serialNumber -> {
            InventoryActivity activityLine = activityRepository.getSerialNoForSaleActivity(inventoryItem.id, serialNumber);

            BigDecimal unitPrice = activityLine.unitPrice;
            BigDecimal sellingPrice = unitPrice.add((unitPrice.multiply(activityLine.inventoryItem.markUp.multiply(BigDecimal.valueOf(0.01)))));

            InventoryActivity inventoryActivity = new InventoryActivity();
            inventoryActivity.transaction = ActivityTransactionTypes.SALE;
            inventoryActivity.date = saleDate.atTime(LocalTime.now());
            inventoryActivity.unitPrice = unitPrice.negate();
            inventoryActivity.sellingPrice = sellingPrice;
            inventoryActivity.costPrice = unitPrice.multiply(BigDecimal.valueOf(1)).negate();
            inventoryActivity.quantity = BigDecimal.valueOf(1).negate();
            inventoryActivity.inventoryItem = inventoryItem;
            inventoryActivity.activityLine = activityLine;

            inventoryActivity.persist();

            activityLine.quantityRemaining = activityLine.quantityRemaining.subtract(BigDecimal.valueOf(1));
            activityLine.persist();

            activities.add(inventoryActivity);
        });

        return activities;
    }

    private InventoryActivity saleItemWithBatchInfo(InventoryItem inventoryItem, SaleItemRequest saleItemRequest, LocalDate saleDate, String method) {

        InventoryActivity activityLine = getBatchActivityLine(inventoryItem.id, method, saleItemRequest.batchNumber);

        BigDecimal unitPrice = activityLine.unitPrice;
        BigDecimal sellingPrice = unitPrice.add((unitPrice.multiply(activityLine.inventoryItem.markUp.multiply(BigDecimal.valueOf(0.01)))));

        if (saleItemRequest.quantity.compareTo(activityLine.quantity) >= 0) {

            InventoryActivity inventoryActivity = new InventoryActivity();
            inventoryActivity.transaction = ActivityTransactionTypes.SALE;
            inventoryActivity.date = saleDate.atTime(LocalTime.now());
            inventoryActivity.unitPrice = unitPrice.negate();
            inventoryActivity.sellingPrice = sellingPrice;
            inventoryActivity.costPrice = unitPrice.multiply(saleItemRequest.quantity).negate();
            inventoryActivity.quantity = saleItemRequest.quantity.negate();
            inventoryActivity.inventoryItem = inventoryItem;
            inventoryActivity.activityLine = activityLine;

            inventoryActivity.persist();

            activityLine.quantityRemaining = activityLine.quantityRemaining.subtract(saleItemRequest.quantity);
            activityLine.persist();

            return inventoryActivity;

        } else {

            throw new WebApplicationException(NO_ITEMS_FOUND);

        }
    }

    private BigDecimal getCostPrice(Long itemId, String method) {

        if (method.equalsIgnoreCase(PaymentTypes.AVG)) {
            return inventoryCommonService.calculateAverageCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.FEFO)) {
            return inventoryCommonService.calculateFEFOCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.FIFO)) {
            return inventoryCommonService.calculateFIFOCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.HIFO)) {
            return inventoryCommonService.calculateHIFOCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.LIFO)) {
            return inventoryCommonService.calculateLIFOCost(itemId).await().indefinitely();

        } else {
            throw new NotFoundException(METHOD_NOT_FOUND);
        }
    }

    private BigDecimal getSellingPrice(Long itemId, String method) {

        if (method.equalsIgnoreCase(PaymentTypes.AVG)) {
            return inventoryCommonService.calculateSellingPriceFromAverageCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.FEFO)) {
            return inventoryCommonService.calculateSellingPriceFromFEFOCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.FIFO)) {
            return inventoryCommonService.calculateSellingPriceFromFIFOCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.HIFO)) {
            return inventoryCommonService.calculateSellingPriceFromHIFOCost(itemId).await().indefinitely();

        } else if (method.equalsIgnoreCase(PaymentTypes.LIFO)) {
            return inventoryCommonService.calculateSellingPriceFromLIFOCost(itemId).await().indefinitely();

        } else {
            throw new NotFoundException(METHOD_NOT_FOUND);
        }
    }

    private InventoryActivity getActivityLine(Long itemId, String method) {

        if (method.equalsIgnoreCase(PaymentTypes.FIFO) || method.equalsIgnoreCase(PaymentTypes.AVG) || method.equalsIgnoreCase(PaymentTypes.FEFO)) {
            return activityRepository.getFirstInForSaleActivity(itemId);

        } else if (method.equalsIgnoreCase(PaymentTypes.HIFO)) {
            return activityRepository.getHighestInForSaleActivity(itemId);

        } else if (method.equalsIgnoreCase(PaymentTypes.LIFO)) {
            return activityRepository.getLastInForSaleActivity(itemId);

        } else {
            throw new NotFoundException(METHOD_NOT_FOUND);
        }

    }

    private InventoryActivity getBatchActivityLine(Long itemId, String method, String batchNumber) {

        if (method.equalsIgnoreCase(PaymentTypes.FIFO) || method.equalsIgnoreCase(PaymentTypes.AVG)) {
            return activityRepository.getFirstInBatchForSaleActivity(itemId, batchNumber);

        } else if (method.equalsIgnoreCase(PaymentTypes.FEFO)) {
            return activityRepository.getFirstExpiryBatchForSaleActivity(itemId, batchNumber);

        } else if (method.equalsIgnoreCase(PaymentTypes.HIFO)) {
            return activityRepository.getHighestInBatchForSaleActivity(itemId, batchNumber);

        } else if (method.equalsIgnoreCase(PaymentTypes.LIFO)) {
            return activityRepository.getLastInBatchForSaleActivity(itemId, batchNumber);

        } else {
            throw new NotFoundException(METHOD_NOT_FOUND);
        }

    }

    public Response getActivities() {
        return Response.ok(activityRepository.listAll()).build();
    }

    public Response deleteActivity(Long id) {
        InventoryActivity inventoryActivity = activityRepository.getById(id);
        inventoryActivity.delete();

        return Response.ok(inventoryActivity).build();
    }
}
