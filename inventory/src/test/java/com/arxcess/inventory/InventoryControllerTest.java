package com.arxcess.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class InventoryControllerTest {

    @Inject
    ObjectMapper objectMapper;

    @Test
    void testCreateRoute() {

        ObjectNode itemJson = objectMapper.createObjectNode();
        itemJson.put("name", "Pencil");
        itemJson.put("barcode", "pencil-123");
        itemJson.put("type", "ITEM");
        itemJson.put("isReturnable", false);

        given().when()
                .body(itemJson)
                .contentType("application/json")
                .post("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString("\"name\":\"Pencil\""));
    }

    @Test
    void testFailToCreateWhenItemExistsRoute() {

        ObjectNode itemJson = objectMapper.createObjectNode();
        itemJson.put("name", "Sugar");
        itemJson.put("barcode", "sugar-123");
        itemJson.put("type", "ITEM");
        itemJson.put("isReturnable", false);

        given().when()
                .body(itemJson)
                .contentType("application/json")
                .post("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString("\"name\":\"Sugar\""));

        ResponsePOJO response = given().when()
                .body(itemJson)
                .contentType("application/json")
                .post("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .as(ResponsePOJO.class);

        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.status);

    }

}
