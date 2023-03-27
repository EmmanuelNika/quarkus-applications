package com.arxcess.inventory.controllers.endpoints;

import com.arxcess.inventory.controllers.services.InventoryService;
import com.arxcess.inventory.controllers.services.payloads.InventoryItemRequest;
import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.Route.HttpMethod;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

@Tag(name = "Inventory")
public class InventoryController {

    @Inject
    InventoryService service;

    @Route(path = "/inventory", methods = HttpMethod.POST, produces = "application/json")
    Uni<Response> createItem(@Body @Valid InventoryItemRequest request) {

        return service.createItem(request);
    }

    @Route(path = "/inventory/:id", methods = HttpMethod.GET, produces = "application/json")
    Uni<Response> getItem(@NotNull @Param("id") Long id) {

        return service.getItem(id);
    }

}
