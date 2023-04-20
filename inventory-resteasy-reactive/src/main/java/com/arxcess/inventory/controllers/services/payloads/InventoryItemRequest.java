package com.arxcess.inventory.controllers.services.payloads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public class InventoryItemRequest {

    @Schema(required = true, example = "Headsets")
    public String name;

    @Schema(example = "11223421")
    public String barcode;

    @Schema(required = true, example = "ITEM", enumeration = "{ITEM, SERVICE}")
    public String type;

    public Boolean isReturnable;

    @Schema(required = true, example = "20", description = "Value as a percentage")
    public BigDecimal markUp;

}
