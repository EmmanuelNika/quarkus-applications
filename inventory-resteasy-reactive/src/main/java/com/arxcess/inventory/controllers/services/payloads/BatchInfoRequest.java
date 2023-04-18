package com.arxcess.inventory.controllers.services.payloads;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

public class BatchInfoRequest {

    @Schema(required = true, example = "BS230")
    public String batchNumber;

    @Schema(required = true, example = "03/02/2022")
    public LocalDate manufacturingDate;

    @Schema(required = true, example = "03/02/2025")
    public LocalDate expiryDate;

}
