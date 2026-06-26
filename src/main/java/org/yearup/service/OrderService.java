package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.time.LocalDateTime;

@Service
public class OrderService {

    /*
    I made a decision to pass in profile service because the fields between profile and order model are similar.
    Both contain key info of the user so why duplicate information in the db when there's already
    data for those fields in profile.
    */

    private static final double FLAT_SHIPPING_RATE = 1.99;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProfileService profileService;

    public OrderService(OrderRepository orderRepository,  OrderLineItemRepository orderLineItemRepository, ShoppingCartService shoppingCartService, ProfileService profileService) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.shoppingCartService = shoppingCartService;
        this.profileService = profileService;
    }

    // Need to make a create order method
    public Order checkOutOrder(int userId) {

        // Getting current shopping cart and user profile by user's id. Creating the order and saving it.
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);

        if (shoppingCart.getItems().isEmpty()) {
            return null;
        }

        Profile userProfile = profileService.getProfileById(userId)
                .orElseThrow(() -> new RuntimeException("No profile found by user id: " + userId));

        Order order = createOrder(userId, userProfile);

        Order savedOrder = orderRepository.save(order);

        createOrderLineItems(savedOrder, shoppingCart);

        // Clears users cart after checking out
        shoppingCartService.deleteProductsFromCart(userId);

        return savedOrder;
    }

    // Helper method that iterates over the shopping cart items
    // and then stores them in orderLineItem in the db
    private void createOrderLineItems(Order savedOrder, ShoppingCart shoppingCart) {

        for(ShoppingCartItem shoppingCartItem : shoppingCart.getItems().values()) {

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(savedOrder.getOrderId());
            orderLineItem.setProductId(shoppingCartItem.getProductId());
            orderLineItem.setSalesPrice(shoppingCartItem.getProduct().getPrice());
            orderLineItem.setQuantity(shoppingCartItem.getQuantity());
            orderLineItem.setDiscount(shoppingCartItem.getDiscountPercent());

            orderLineItemRepository.save(orderLineItem);
        }
    }

    // Another helper method
    private Order createOrder(int userId, Profile userProfile) {

        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());
        order.setAddress(userProfile.getAddress());
        order.setCity(userProfile.getCity());
        order.setState(userProfile.getState());
        order.setZip(userProfile.getZip());
        order.setShippingAmount(FLAT_SHIPPING_RATE);

        return order;
    }
}
