package com.arxcess.inventory.controllers.services;

import com.arxcess.inventory.domains.InventoryItem;
import com.arxcess.inventory.domains.repository.InventoryItemRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@ApplicationScoped
public class MarginPriceService {

    @Inject
    InventoryItemRepository itemRepository;

    @Inject
    InventoryCommonService inventoryCostService;

    public Uni<Response> getFromAverageCost(Long id) {

        return inventoryCostService.calculateSellingPrice(id).onItem().ifNotNull().transform(sellingPrice -> Response.ok(sellingPrice).build());

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
