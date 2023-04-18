package com.arxcess.inventory.controllers.endpoints;

import com.arxcess.inventory.controllers.services.TransactionService;
import com.arxcess.inventory.controllers.services.payloads.ReceiveRequest;
import com.arxcess.inventory.controllers.services.payloads.SaleRequest;
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


@Path("transactions")
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "Transaction")
public class TransactionController {

    @Inject
    TransactionService service;

    @POST
    @Path("receive")
    @Transactional
    @APIResponse(responseCode = "201", description = "Save an inventory item receive", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = InventoryItem.class)))
    public Response createReceive(@Valid ReceiveRequest request) {
        return service.createReceive(request);
    }

    @POST
    @Path("sale")
    @Transactional
    @APIResponse(responseCode = "201", description = "Save an inventory item sale", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = InventoryItem.class)))
    public Response createSale(@Valid SaleRequest request) {
        return service.createSale(request);
    }

    @GET
    @APIResponse(responseCode = "200", description = "Get list of inventory item receives", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = InventoryItem.class)))
    public Response get() {
        return service.getActivities();
    }

    @DELETE
    @Path("{id: \\d+}")
    @APIResponse(responseCode = "201", description = "Delete an inventory item receive", content = @Content(mediaType =
            "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = InventoryItem.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Response delete(@PathParam("id") Long id) {
        return service.deleteActivity(id);
    }

}
