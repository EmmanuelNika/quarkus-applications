package com.arxcess.inventory.controllers.services.payloads;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReceiveRequest {

    public LocalDate date;

    public List<ReceiveItemRequest> itemRequests = new ArrayList<>();

}
