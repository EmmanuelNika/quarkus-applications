package com.arxcess.inventory.controllers.services.payloads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;

public class BatchInfoRequest {

    @Schema(required = true, example = "BS230")
    public String batchNumber;

    @Schema(required = true, example = "03/02/2022")
    @JsonbDateFormat(value = "dd/MM/yyyy")
    public LocalDate manufacturingDate;

    @Schema(required = true, example = "03/02/2023")
    @JsonbDateFormat(value = "dd/MM/yyyy")
    public LocalDate expiryDate;

}
