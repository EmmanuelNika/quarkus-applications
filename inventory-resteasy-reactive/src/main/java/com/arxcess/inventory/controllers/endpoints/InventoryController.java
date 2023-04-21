package com.arxcess.inventory.controllers.endpoints;

import com.arxcess.inventory.controllers.services.InventoryService;
import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import com.arxcess.inventory.domains.InventoryItem;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("items")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Inventory")
public class InventoryController {

    @Inject
    InventoryService service;

    @POST
    @Transactional
    @APIResponse(responseCode = "201", description = "Save an inventory item", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = InventoryItem.class)))
    public Response create(@Valid InventoryItemRequest request) {
        return service.createItem(request);
    }

    @GET
    @APIResponse(responseCode = "200", description = "Get list of inventory items", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = InventoryItem.class)))
    public Response get() {
        return service.getItems();
    }

    @GET
    @Path("{id: \\d+}")
    @APIResponse(responseCode = "200", description = "Get an inventory item", content = @Content(mediaType =
            "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = InventoryItem.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Response getSingle(@PathParam("id") Long id) {
        return service.getItem(id);
    }

    @PUT
    @Path("{id: \\d+}")
    @Transactional
    @APIResponse(responseCode = "201", description = "Update an inventory item", content = @Content(mediaType =
            "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = InventoryItem.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Response update(@PathParam("id") Long id, @Valid InventoryItemRequest request) {
        return service.updateItem(id, request);
    }

    @DELETE
    @Path("{id: \\d+}")
    @APIResponse(responseCode = "201", description = "Delete an inventory item", content = @Content(mediaType =
            "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = InventoryItem.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Response delete(@PathParam("id") Long id) {
        return service.deleteItem(id);
    }

}
