package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryCheckRequest;
import com.example.orderservice.dto.InventoryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

;import java.util.List;

@Service
public class InventoryService {
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Value("${inventoryService.url}")
    private String INVENTORY_SERVICE_URL; // Update with actual URL


    @Autowired
    public InventoryService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public void overrideBaseUrl(String url) {
    this.INVENTORY_SERVICE_URL = url;
    }

    public boolean checkInventory(InventoryCheckRequest request) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request),headers);

        ResponseEntity<InventoryResponse> response = restTemplate.exchange(
                INVENTORY_SERVICE_URL + "/api/v1/inventory-manager/check",
                HttpMethod.PUT,
                requestEntity,
                InventoryResponse.class
        );
        return response.getBody() != null && response.getBody().isAvailable();
    }

    public void updateInventory(String product, int quantity) {
        restTemplate.postForObject(
                INVENTORY_SERVICE_URL + "/api/v1/inventory-manager/update",
                new InventoryCheckRequest(product, quantity),
                InventoryCheckRequest.class
        );
    }

}
