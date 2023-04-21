package com.arxcess.inventory.controllers.services.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReceiveRequest {

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(required = true, example = "04/04/2023")
    public LocalDate date;

    public List<ReceiveItemRequest> itemRequests = new ArrayList<>();

}
