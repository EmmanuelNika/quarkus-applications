package com.arxcess.inventory.controllers.services.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

public class BatchInfoRequest {

    @Schema(required = true, example = "BS230")
    public String batchNumber;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(required = true, example = "03/02/2022")
    public LocalDate manufacturingDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(required = true, example = "03/02/2023")
    public LocalDate expiryDate;

}
