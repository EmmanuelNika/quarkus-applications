package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.controllers.endpoints.InventoryController;
import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.repository.InventoryItemRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@ApplicationScoped
public class InventoryService {

    @Inject
    InventoryItemRepository itemRepository;

    public Response createItem(InventoryItemRequest request) {

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.name = request.name;
        inventoryItem.barcode = request.barcode;
        inventoryItem.isReturnable = request.isReturnable;
        inventoryItem.type = request.type;
        inventoryItem.markUp = request.markUp;
        inventoryItem.persist();

        return Response.created(UriBuilder.fromResource(InventoryController.class).path("/" + inventoryItem.id).build()).entity(inventoryItem).build();

    }

    public Response getItems() {
        return Response.ok(itemRepository.listAll()).build();
    }

    public Response getItem(Long id) {
        return Response.ok(itemRepository.getById(id)).build();
    }

    public Response updateItem(Long id, InventoryItemRequest request) {

        InventoryItem inventoryItem = itemRepository.getById(id);
        inventoryItem.name = request.name;
        inventoryItem.barcode = request.barcode;
        inventoryItem.isReturnable = request.isReturnable;
        inventoryItem.type = request.type;
        inventoryItem.markUp = request.markUp;
        inventoryItem.persist();

        return Response.noContent().build();
    }

    public Response deleteItem(Long id) {
        InventoryItem inventoryItem = itemRepository.getById(id);
        inventoryItem.delete();

        return Response.ok().entity(inventoryItem).build();
    }
}
