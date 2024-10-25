package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryCheckRequest;
import com.example.orderservice.dto.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryService {
    private final RestTemplate restTemplate;

    @Value("${inventoryService.url}")
    private String INVENTORY_SERVICE_URL; // Update with actual URL


    @Autowired
    public InventoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean checkInventory(InventoryCheckRequest request) {
        InventoryResponse response = restTemplate.postForObject(
                INVENTORY_SERVICE_URL + "/check",
                request,
                InventoryResponse.class
        );
        return response != null && response.isAvailable();
    }

    public void updateInventory(String product, int quantity) {
        restTemplate.postForObject(
                INVENTORY_SERVICE_URL + "/update",
                new InventoryCheckRequest(product, quantity),
                Void.class
        );
    }

}
