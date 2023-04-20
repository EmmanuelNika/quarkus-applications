package com.arxcess.inventory.controllers.services;

import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class CostService {

    @Inject
    InventoryCommonService inventoryCostService;

    public Uni<Response> getAverageCost(Long id) {

        return inventoryCostService.calculateAverageCost(id).onItem().transform(cost -> Response.ok(cost).build());

    }

    public Response getHIFOCost(Long id) {

        return Response.ok().build();
    }

    public Response getFIFOCost(Long id) {

        return Response.ok().build();
    }

    public Response getLIFOCost(Long id) {

        return Response.ok().build();
    }

    public Uni<Response> getFEFOCost(Long id) {

        return inventoryCostService.calculateFEFOCost(id).onItem().transform(cost -> Response.ok(cost).build());
    }

}
