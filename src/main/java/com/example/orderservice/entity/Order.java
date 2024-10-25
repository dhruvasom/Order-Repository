package com.example.orderservice.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "orders")  // Optional, specify table name
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String product;
    private int quantity;
    private double price;

    // Constructors, getters, and setters
//    public Order() {}

//    public Order(String product, int quantity, double price) {
//        this.product = product;
//        this.quantity = quantity;
//        this.price = price;
//    }

    public Order(Long id, String product, int quantity, double price) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Order() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

