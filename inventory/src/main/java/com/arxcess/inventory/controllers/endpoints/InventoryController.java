package com.arxcess.inventory.controllers.endpoints;

import com.arxcess.inventory.controllers.services.InventoryService;
import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Tag(name = "Inventory")
public class InventoryController {

    @Inject
    InventoryService service;

    @Route(path = "/items", methods = HttpMethod.POST, produces = "application/json")
    @APIResponse(responseCode = "201", description = "Save an inventory item", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> createItem(@Body @Valid InventoryItemRequest request) {

        return service.createItem(request);
    }

    @Route(path = "/items", methods = HttpMethod.GET, produces = "application/json")
    @APIResponse(responseCode = "200", description = "Get list of inventory items", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> getItems() {

        return service.getItems();
    }

    @Route(path = "/items/:id", methods = HttpMethod.GET, produces = "application/json")
    @APIResponse(responseCode = "200", description = "Get an inventory item", content = @Content(mediaType =
            "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> getItem(@NotNull @Param("id") Long id) {

        return service.getItem(id);
    }

    @Route(path = "/items/:id", methods = HttpMethod.PUT, produces = "application/json")
    @APIResponse(responseCode = "201", description = "Update an inventory item", content = @Content(mediaType =
            "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> updateItem(@NotNull @Param("id") Long id, @Body @Valid InventoryItemRequest request) {

        return service.updateItem(id, request);
    }

    @Route(path = "/items/:id", methods = HttpMethod.DELETE, produces = "application/json")
    @APIResponse(responseCode = "201", description = "Delete an inventory item", content = @Content(mediaType =
            "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType =
            MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> deleteItem(@NotNull @Param("id") Long id) {

        return service.delete(id);
    }

}
