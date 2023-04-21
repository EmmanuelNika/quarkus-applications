package com.arxcess.inventory.controllers.services.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRequest {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(required = true, example = "10/04/2023")
    public LocalDate date;

    @Schema(example = "Average", enumeration = "{Average, FEFO, FIFO, HIFO, LIFO}")
    public String method;

    public List<SaleItemRequest> itemRequests = new ArrayList<>();

}
