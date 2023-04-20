package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import com.arxcess.inventory.controllers.services.payloads.ReceiveItemRequest;
import com.arxcess.inventory.controllers.services.payloads.ReceiveRequest;
import com.arxcess.inventory.controllers.services.statics.ActivityTransactionTypes;
import com.arxcess.inventory.domains.BatchInfo;
import com.arxcess.inventory.domains.InventoryActivity;
import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.repository.BatchInfoRepository;
import com.arxcess.inventory.domains.repository.InventoryActivityRepository;
import com.arxcess.inventory.domains.repository.InventoryItemRepository;
import com.arxcess.inventory.handlers.CustomErrorHandler;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationScoped
public class InventoryReceiveService {

    private static final String NOT_FOUND = "Item not found!";

    @Inject
    InventoryItemRepository itemRepository;

    @Inject
    InventoryActivityRepository activityRepository;

    @Inject
    BatchInfoRepository batchInfoRepository;

    public Uni<Response> getReceives() {

        return activityRepository.listAll().onItem().transform(activities -> Response.ok(activities).build());
    }

    public Uni<Response> createReceive(ReceiveRequest request) {

        List<InventoryActivity> activities = new ArrayList<>();

        request.itemRequests.forEach(receiveItemRequest -> itemRepository.findById(receiveItemRequest.itemId).onItem().transform(inventoryItem -> {

            if (Boolean.TRUE.equals(receiveItemRequest.serialNumbers.isEmpty()) && receiveItemRequest.batchInfo == null) {
                InventoryActivity inventoryActivity = new InventoryActivity();
                inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                inventoryActivity.date = request.date.atTime(LocalTime.now());
                inventoryActivity.unitPrice = receiveItemRequest.costPrice;
                inventoryActivity.costPrice = receiveItemRequest.costPrice.multiply(receiveItemRequest.quantity);
                inventoryActivity.quantity = receiveItemRequest.quantity;
                inventoryActivity.inventoryItem = inventoryItem;

                activities.add(inventoryActivity);

            } else if (Boolean.FALSE.equals(receiveItemRequest.serialNumbers.isEmpty()) && receiveItemRequest.batchInfo == null) {

                receiveItemRequest.serialNumbers.forEach(serialNumber -> {
                    InventoryActivity inventoryActivity = new InventoryActivity();
                    inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                    inventoryActivity.date = request.date.atTime(LocalTime.now());
                    inventoryActivity.unitPrice = receiveItemRequest.costPrice;
                    inventoryActivity.costPrice = receiveItemRequest.costPrice;
                    inventoryActivity.quantity = BigDecimal.valueOf(1);
                    inventoryActivity.inventoryItem = inventoryItem;

                    activities.add(inventoryActivity);

                });

            } else {

                InventoryActivity inventoryActivity = new InventoryActivity();
                inventoryActivity.transaction = ActivityTransactionTypes.RECEIVE;
                inventoryActivity.date = request.date.atTime(LocalTime.now());
                inventoryActivity.unitPrice = receiveItemRequest.costPrice;
                inventoryActivity.costPrice = receiveItemRequest.costPrice.multiply(receiveItemRequest.quantity);
                inventoryActivity.quantity = receiveItemRequest.quantity;
                inventoryActivity.inventoryItem = inventoryItem;

                BatchInfo batchInfo = new BatchInfo();
                batchInfo.batchReference = batchInfoRepository.generateBatchReference(request.date);
                batchInfo.batchNumber = receiveItemRequest.batchInfo.batchNumber;
                batchInfo.manufacturingDate = receiveItemRequest.batchInfo.manufacturingDate;
                batchInfo.expiryDate = receiveItemRequest.batchInfo.expiryDate;
                batchInfo.inventoryItem = inventoryItem;

                Panache.withTransaction(batchInfo::persist);

                activities.add(inventoryActivity);
            }

            return inventoryItem;

        }).onFailure().retry().atMost(3).subscribe().with(act -> System.out.println("Added to list"), err -> System.out.println("No items added to list")));

        return Panache.withTransaction(() -> activityRepository.persist(activities)).ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(IllegalStateException::new)
                .replaceWith(Response.created(URI.create("/receives")).status(Status.CREATED).entity(activities).build());

    }

    public Uni<Response> getItem(Long id) {

        return itemRepository.findById(id)
                .onItem()
                .ifNotNull()
                .transform(inventoryItem -> Response.ok(inventoryItem).build())
                .onItem()
                .ifNull()
                .fail()
                .onFailure()
                .recoverWithItem(CustomErrorHandler.notFound(NOT_FOUND));
    }

    public Uni<Response> updateItem(Long id, InventoryItemRequest request) {

        return itemRepository.validateRequest(id, request.name, request.barcode)
                .onItem()
                .ifNotNull()
                .transform(t -> Response.ok()
                        .status(Status.BAD_REQUEST)
                        .entity("Item already exists!")
                        .build())
                .onItem()
                .ifNull()
                .switchTo(() -> Panache.withTransaction(() -> itemRepository.findById(id)
                                .onItem()
                                .ifNotNull()
                                .invoke(inventoryItem -> {
                                    inventoryItem.name = request.name;
                                    inventoryItem.type = request.type;
                                    inventoryItem.barcode = request.barcode;
                                    inventoryItem.isReturnable = request.isReturnable;
                                }))
                        .onItem()
                        .ifNotNull()
                        .transform(entity -> Response.ok(entity).build())
                        .onItem()
                        .ifNull()
                        .continueWith(CustomErrorHandler.notFound(NOT_FOUND)));

    }

    public Uni<Response> delete(Long id) {

        return Panache.withTransaction(() -> itemRepository.deleteById(id))
                .map(deleted -> Boolean.TRUE.equals(deleted) ? Response.ok()
                        .status(Status.NO_CONTENT)
                        .build() : CustomErrorHandler.notFound(NOT_FOUND));
    }

}
