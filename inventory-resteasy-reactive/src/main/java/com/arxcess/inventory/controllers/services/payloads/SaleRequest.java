package com.arxcess.inventory.controllers.services.payloads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRequest {

    @Schema(required = true, example = "10/04/2023")
    @JsonbDateFormat(value = "dd/MM/yyyy")
    public LocalDate date;

    @Schema(example = "Average", enumeration = "{Average, FEFO, FIFO, HIFO, LIFO}")
    public String method;

    public List<SaleItemRequest> itemRequests = new ArrayList<>();

}
