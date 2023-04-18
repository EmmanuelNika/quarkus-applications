package com.arxcess.inventory.controllers.services.payloads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class InventoryItemRequest {

    @Schema(required = true, example = "Headsets")
    public String name;

    @Schema(example = "11223421")
    public String barcode;

    @Schema(required = true, example = "ITEM", enumeration = "{ITEM, SERVICE}")
    public String type;

    public Boolean isReturnable;

}
