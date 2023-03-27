package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.repository.InventoryItemRepository;
import com.arxcess.inventory.handlers.CustomErrorHandler;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@ApplicationScoped
public class InventoryService {

    @Inject
    InventoryItemRepository itemRepository;

    public Uni<Response> createItem(InventoryItemRequest request) {

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.name = request.name;
        inventoryItem.type = request.type;
        inventoryItem.barcode = request.barcode;
        inventoryItem.isReturnable = request.isReturnable;

        return Panache.withTransaction(inventoryItem::persist)
                .replaceWith(Response.ok()
                        .status(Status.CREATED)
                        .entity(inventoryItem)
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
                .recoverWithItem(CustomErrorHandler.notFound("Item not found"));
    }

    public Uni<Response> byId(Long id) {

        System.out.println("By id");

        return itemRepository.findById(id)
                .onItem()
                .ifNull()
                .failWith(new NotFoundException("Item not found"))
                .onFailure()
                .transform(throwable -> new NotFoundException(throwable.getMessage()))
                .replaceWith(Response
                        .status(Status.BAD_GATEWAY)
                        .build());
    }

}
