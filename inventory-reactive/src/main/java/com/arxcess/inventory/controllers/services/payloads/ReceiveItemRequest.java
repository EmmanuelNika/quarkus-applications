package com.arxcess.inventory.controllers.services.payloads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReceiveItemRequest {

    @Schema(required = true, example = "5")
    public BigDecimal quantity;

    @Schema(required = true, example = "1000")
    public BigDecimal costPrice;

    @Schema(required = true, example = "1")
    public Long itemId;

    public List<String> serialNumbers = new ArrayList<>();

    public BatchInfoRequest batchInfo;

}
