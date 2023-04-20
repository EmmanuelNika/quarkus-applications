package com.arxcess.inventory.controllers.services.payloads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReceiveRequest {

    @Schema(required = true, example = "04/04/2023")
    @JsonbDateFormat("dd/MM/yyyy")
    public LocalDate date;

    public List<ReceiveItemRequest> itemRequests = new ArrayList<>();

}
