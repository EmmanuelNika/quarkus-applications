package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.controllers.endpoints.ReceiveController;
import com.arxcess.inventory.controllers.services.payloads.ReceiveRequest;
import com.arxcess.inventory.controllers.services.statics.ActivityTransactionTypes;
import com.arxcess.inventory.domains.BatchInfo;
import com.arxcess.inventory.domains.InventoryActivity;
import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.InventoryItemSerialNumber;
import com.arxcess.inventory.domains.repository.BatchInfoRepository;
import com.arxcess.inventory.domains.repository.InventoryActivityRepository;
import com.arxcess.inventory.domains.repository.InventoryItemRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ReceiveService {

    @Inject
    InventoryItemRepository itemRepository;

    @Inject
    BatchInfoRepository batchInfoRepository;

    @Inject
    InventoryActivityRepository activityRepository;

    public Response createReceive(ReceiveRequest request) {

        List<InventoryActivity> activities = new ArrayList<>();

        request.itemRequests.forEach(receiveItemRequest -> {
            InventoryItem inventoryItem = itemRepository.getById(receiveItemRequest.itemId);

            if (Boolean.TRUE.equals(receiveItemRequest.serialNumbers.isEmpty()) && receiveItemRequest.batchInfo == null) {
                InventoryActivity inventoryActivity = new InventoryActivity();
                inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                inventoryActivity.date = request.date.atTime(LocalTime.now());
                inventoryActivity.costPrice = receiveItemRequest.costPrice;
                inventoryActivity.quantity = receiveItemRequest.quantity;
                inventoryActivity.inventoryItem = inventoryItem;

                inventoryActivity.persist();

                activities.add(inventoryActivity);

            } else if (Boolean.FALSE.equals(receiveItemRequest.serialNumbers.isEmpty()) && receiveItemRequest.batchInfo == null) {

                receiveItemRequest.serialNumbers.forEach(serialNumber -> {
                    InventoryActivity inventoryActivity = new InventoryActivity();
                    inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                    inventoryActivity.date = request.date.atTime(LocalTime.now());
                    inventoryActivity.costPrice = receiveItemRequest.costPrice;
                    inventoryActivity.quantity = BigDecimal.valueOf(1);
                    inventoryActivity.inventoryItem = inventoryItem;

                    InventoryItemSerialNumber itemSerialNumber = new InventoryItemSerialNumber();
                    itemSerialNumber.serialNumber = serialNumber;
                    itemSerialNumber.inventoryItem = inventoryItem;
                    itemSerialNumber.persist();

                    inventoryActivity.persist();

                    activities.add(inventoryActivity);

                });

            } else {

                InventoryActivity inventoryActivity = new InventoryActivity();
                inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                inventoryActivity.date = request.date.atTime(LocalTime.now());
                inventoryActivity.costPrice = receiveItemRequest.costPrice;
                inventoryActivity.quantity = receiveItemRequest.quantity;
                inventoryActivity.inventoryItem = inventoryItem;

                BatchInfo batchInfo = new BatchInfo();
                batchInfo.batchReference = batchInfoRepository.generateBatchReference(request.date);
                batchInfo.batchNumber = receiveItemRequest.batchInfo.batchNumber;
                batchInfo.manufacturingDate = receiveItemRequest.batchInfo.manufacturingDate;
                batchInfo.expiryDate = receiveItemRequest.batchInfo.expiryDate;
                batchInfo.inventoryItem = inventoryItem;
                batchInfo.persist();

                inventoryActivity.persist();

                activities.add(inventoryActivity);
            }
        });

        return Response.created(UriBuilder.fromResource(ReceiveController.class).path("").build()).entity(activities).build();

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
