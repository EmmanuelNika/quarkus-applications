package com.arxcess.inventory.controllers.services;

import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class MarginPriceService {

    @Inject
    InventoryCommonService inventoryCostService;

    public Uni<Response> getFromAverageCost(Long id) {

        return inventoryCostService.calculateSellingPriceFromAverageCost(id).onItem().ifNotNull().transform(sellingPrice -> Response.ok(sellingPrice).build());

    }

    public Response getFromHIFOCost(Long id) {

        return Response.ok().build();
    }

    public Response getFromFIFOCost(Long id) {

        return Response.ok().build();
    }

    public Response getFromLIFOCost(Long id) {

        return Response.ok().build();
    }

    public Response getFromFEFOCost(Long id) {

        return Response.ok().build();
    }

}
