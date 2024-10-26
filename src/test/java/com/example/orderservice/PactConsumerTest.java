package com.example.orderservice;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.InventoryService;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "InventoryCatalogue")
public class PactConsumerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Pact(consumer = "OrderConsumer")
    public V4Pact checkInventory(PactDslWithProvider builder) {
        PactDslJsonBody inventoryItem = new PactDslJsonBody()
                .stringType("name", "Laptop")
                .integerType("quantity", 1);

        return builder.given("Product is available in stock")
                .uponReceiving("Check availability to order N units of Product")
                .path("/api/v1/inventory-manager/check")
                .method("PUT")
                .headers(Map.of("Content-Type", "application/json"))
                .body(inventoryItem)
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .booleanType("available", true)).toPact(V4Pact.class);
    }

    @Pact(consumer = "OrderConsumer")
    public V4Pact updateInventory(PactDslWithProvider builder) {
        PactDslJsonBody inventoryItem = new PactDslJsonBody()
            .stringType("name", "Laptop")
            .integerType("quantity", 1);
        return builder.given("Product is available in stock")
                .uponReceiving("Update the stock after order placed")
                .path("/api/v1/inventory-manager/update")
                .method("POST")
                .headers(Map.of("Content-Type", "application/json"))
                .body(inventoryItem)
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(inventoryItem)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethods = {"checkInventory","updateInventory"}, port = "9999")
    public void test(MockServer mockServer) throws JsonProcessingException {
        inventoryService.overrideBaseUrl(mockServer.getUrl());
        Order order = new Order(1L, "Laptop", 1, 11.11);
        orderService.createOrder(order);
    }

}
