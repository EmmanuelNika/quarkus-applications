package com.arxcess.inventory.controllers.endpoints;

import com.arxcess.inventory.controllers.services.InventoryReceiveService;
import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import com.arxcess.inventory.controllers.services.payloads.ReceiveRequest;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
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

@Tag(name = "Receive")
public class ReceiveController {

    @Inject
    InventoryReceiveService service;

    @Route(path = "/receives", methods = HttpMethod.GET, produces = "application/json")
    @APIResponse(responseCode = "200", description = "Get list of inventory item receives", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> getItems() {

        return service.getReceives();
    }

    @Route(path = "/receives", methods = HttpMethod.POST, produces = "application/json")
    @APIResponse(responseCode = "201", description = "Save an inventory item receive", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> createItem(@Body @Valid ReceiveRequest request) {

        return service.createReceive(request);
    }

    @Route(path = "/receives/:id", methods = HttpMethod.GET, produces = "application/json")
    @APIResponse(responseCode = "200", description = "Get an inventory item receive", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> getItem(@NotNull @Param("id") Long id) {

        return service.getItem(id);
    }

    @Route(path = "/receives/:id", methods = HttpMethod.PUT, produces = "application/json")
    @APIResponse(responseCode = "201", description = "Update an inventory item receive", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> updateItem(@NotNull @Param("id") Long id, @Body @Valid InventoryItemRequest request) {

        return service.updateItem(id, request);
    }

    @Route(path = "/receives/:id", methods = HttpMethod.DELETE, produces = "application/json")
    @APIResponse(responseCode = "201", description = "Delete an inventory item receive", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    @APIResponse(responseCode = "400", description = "Item not found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = Response.class)))
    public Uni<Response> deleteItem(@NotNull @Param("id") Long id) {

        return service.delete(id);
    }

}
