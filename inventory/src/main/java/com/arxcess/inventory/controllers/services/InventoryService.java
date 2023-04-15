package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.repository.InventoryItemRepository;
import com.arxcess.inventory.handlers.CustomErrorHandler;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.time.Duration;

@ApplicationScoped
public class InventoryService {

    private static final String NOT_FOUND = "Item not found!";

    @Inject
    InventoryItemRepository itemRepository;

    public Uni<Response> createItem(InventoryItemRequest request) {

        return itemRepository.validateRequest(request.name, request.barcode)
                .onItem()
                .ifNotNull()
                .transform(t -> Response.ok()
                        .status(Status.BAD_REQUEST)
                        .entity("Item already exists!")
                        .build())
                .onItem()
                .ifNull()
                .switchTo(() -> {
                    InventoryItem inventoryItem = new InventoryItem();
                    inventoryItem.name = request.name;
                    inventoryItem.type = request.type;
                    inventoryItem.barcode = request.barcode;
                    inventoryItem.isReturnable = request.isReturnable;

                    return Panache.withTransaction(inventoryItem::persist)
                            .replaceWith(inventoryItem)
                            .ifNoItem()
                            .after(Duration.ofMillis(10000))
                            .fail()
                            .onFailure()
                            .transform(IllegalStateException::new)
                            .replaceWith(Response.created(URI.create("/items/" + inventoryItem.id))
                                    .status(Status.CREATED)
                                    .entity(inventoryItem)
                                    .build());
                });
    }

    public Uni<Response> getItems() {

        return itemRepository.listAll()
                .onItem()
                .transform(inventoryItems -> Response.ok(inventoryItems)
                        .build());
    }

    public Uni<Response> getItem(Long id) {

        return itemRepository.findById(id)
                .onItem()
                .ifNotNull()
                .transform(inventoryItem -> Response.ok(inventoryItem)
                        .build())
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
                        .transform(entity -> Response.ok(entity)
                                .build())
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
