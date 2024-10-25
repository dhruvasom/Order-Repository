package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryCheckRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    public OrderService(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public Order createOrder(Order order) {
        InventoryCheckRequest request = new InventoryCheckRequest(order.getProduct(), order.getQuantity());
        System.out.println(inventoryService.checkInventory(request));
        // Check inventory before creating the order
        if (inventoryService.checkInventory(request)) {
            order = orderRepository.save(order);
            inventoryService.updateInventory(order.getProduct(), order.getQuantity());
            return order;
        } else {
            throw new RuntimeException("Insufficient inventory for name: " + order.getProduct());
        }
    }

    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        InventoryCheckRequest request = new InventoryCheckRequest(orderDetails.getProduct(), orderDetails.getQuantity());

        // Check inventory before updating the order
        if (inventoryService.checkInventory(request)) {
            order.setProduct(orderDetails.getProduct());
            order.setQuantity(orderDetails.getQuantity());
            order.setPrice(orderDetails.getPrice());
            orderRepository.save(order);
            inventoryService.updateInventory(order.getProduct(), order.getQuantity());
            return order;
        } else {
            throw new RuntimeException("Insufficient inventory for name: " + orderDetails.getProduct());
        }
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostConstruct
    public void seedDB() {
        orderRepository.save(new Order(1L,"Laptop",2,2.2));
    }
}
