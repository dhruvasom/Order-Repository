package com.example.orderservice.dto;

public record InventoryResponse(boolean available) {
    public boolean isAvailable() {
        return available;
    }
}
