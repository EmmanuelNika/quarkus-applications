package com.arxcess.inventory.controllers.services.payloads;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRequest {

    public LocalDate date;

    public String method;

    public List<SaleItemRequest> itemRequests = new ArrayList<>();

}
