package com.arxcess.inventory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class InventoryControllerTest {

    Faker faker = new Faker();

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

    @Test
    void testGetList() {

        getDataList().forEach(itemJson -> given().when()
                .body(itemJson)
                .contentType("application/json")
                .post("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString(String.format("\"name\":%s", itemJson.get("name")))));


        ResponsePOJO response = given().when()
                .contentType("application/json")
                .get("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .as(ResponsePOJO.class);

        assertEquals(Status.OK.getStatusCode(), response.status);

    }

    @Test
    void testGetSingleItem() {

        ObjectNode itemJson = getData();

        ResponsePOJO responsePOJO = given().when()
                .body(itemJson)
                .contentType("application/json")
                .post("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString(String.format("\"name\":%s", itemJson.get("name"))))
                .extract()
                .as(ResponsePOJO.class);

        JsonNode jsonNode = objectMapper.convertValue(responsePOJO.entity, JsonNode.class);

        ResponsePOJO response = given().when()
                .contentType("application/json")
                .get("/items/" + jsonNode.get("id"))
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString(String.format("\"name\":%s", itemJson.get("name"))))
                .extract()
                .as(ResponsePOJO.class);

        assertEquals(Status.OK.getStatusCode(), response.status);

    }

    @Test
    void testNotFoundWhenGettingSingleItemWithFakeId() {

        ResponsePOJO response = given().when()
                .contentType("application/json")
                .get("/items/" + 1203832403)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .as(ResponsePOJO.class);

        assertEquals(Status.NOT_FOUND.getStatusCode(), response.status);

    }

    @Test
    void testUpdateSingleItem() {

        ObjectNode savedItemJson = getData();

        ResponsePOJO savedResponse = given().when()
                .body(savedItemJson)
                .contentType("application/json")
                .post("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString(String.format("\"name\":%s",
                        savedItemJson.get("name"))))
                .extract()
                .as(ResponsePOJO.class);

        JsonNode savedNode = objectMapper.convertValue(savedResponse.entity, JsonNode.class);

        ResponsePOJO response = given().when()
                .contentType("application/json")
                .get("/items/" + savedNode.get("id"))
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString(String.format("\"name\":%s",
                        savedItemJson.get("name"))))
                .extract()
                .as(ResponsePOJO.class);

        assertEquals(Status.OK.getStatusCode(), response.status);

        savedItemJson.put("name", faker.beer()
                .name());
        savedItemJson.put("barcode", faker.code()
                .ean8());
        savedItemJson.put("type", "ITEM");
        savedItemJson.put("isReturnable", true);

        ResponsePOJO updateResponse = given().when()
                .body(savedItemJson)
                .contentType("application/json")
                .put("/items/" + savedNode.get("id"))
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString(String.format("\"name\":%s",
                        savedItemJson.get("name"))))
                .extract()
                .as(ResponsePOJO.class);

        JsonNode updatedNode = objectMapper.convertValue(updateResponse.entity, JsonNode.class);

        assertEquals(savedItemJson.get("name"), updatedNode.get("name"));

    }

    @Test
    void testDeleteItem() {

        ObjectNode itemJson = getData();

        ResponsePOJO responsePOJO = given().when()
                .body(itemJson)
                .contentType("application/json")
                .post("/items")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .body(containsString("\"id\":"), containsString(String.format("\"name\":%s", itemJson.get("name"))))
                .extract()
                .as(ResponsePOJO.class);

        JsonNode jsonNode = objectMapper.convertValue(responsePOJO.entity, JsonNode.class);

        ResponsePOJO response = given().when()
                .contentType("application/json")
                .delete("/items/" + jsonNode.get("id"))
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract()
                .as(ResponsePOJO.class);

        assertEquals(Status.NO_CONTENT.getStatusCode(), response.status);

    }


    public List<ObjectNode> getDataList() {

        List<ObjectNode> res = new ArrayList<>();

        ObjectNode item1 = objectMapper.createObjectNode();
        item1.put("name", faker.beer()
                .name());
        item1.put("barcode", faker.code()
                .ean8());
        item1.put("type", "ITEM");
        item1.put("isReturnable", false);

        ObjectNode item2 = objectMapper.createObjectNode();
        item2.put("name", faker.beer()
                .name());
        item2.put("barcode", faker.code()
                .ean8());
        item2.put("type", "ITEM");
        item2.put("isReturnable", false);

        ObjectNode item3 = objectMapper.createObjectNode();
        item3.put("name", faker.beer()
                .name());
        item3.put("barcode", faker.code()
                .ean8());
        item3.put("type", "ITEM");
        item3.put("isReturnable", false);

        res.add(item1);
        res.add(item2);
        res.add(item3);

        return res;
    }

    public ObjectNode getData() {

        ObjectNode itemJson = objectMapper.createObjectNode();
        itemJson.put("name", faker.beer()
                .name());
        itemJson.put("barcode", faker.code()
                .ean8());
        itemJson.put("type", "ITEM");
        itemJson.put("isReturnable", false);

        return itemJson;
    }

}
