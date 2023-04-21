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

        return inventoryCostService.calculateSellingPriceFromAverageCost(id).onItem().ifNotNull()
                .transform(sellingPrice -> Response.ok(sellingPrice).build());

    }

    public Uni<Response> getFromHIFOCost(Long id) {

        return inventoryCostService.calculateSellingPriceFromHIFOCost(id).onItem().ifNotNull()
                .transform(sellingPrice -> Response.ok(sellingPrice).build());
    }

    public Uni<Response> getFromFIFOCost(Long id) {

        return inventoryCostService.calculateSellingPriceFromFIFOCost(id).onItem().ifNotNull()
                .transform(sellingPrice -> Response.ok(sellingPrice).build());
    }

    public Uni<Response> getFromLIFOCost(Long id) {

        return inventoryCostService.calculateSellingPriceFromLIFOCost(id).onItem().ifNotNull()
                .transform(sellingPrice -> Response.ok(sellingPrice).build());
    }

    public Uni<Response> getFromFEFOCost(Long id) {

        return inventoryCostService.calculateSellingPriceFromFEFOCost(id).onItem().ifNotNull()
                .transform(sellingPrice -> Response.ok(sellingPrice).build());
    }

}
