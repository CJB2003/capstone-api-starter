package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public Order createOrder(int userId) {

        // Getting shopping cart and profile by user's id
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);
        Profile userProfile = profileService.getProfileById(userId)
                .orElseThrow(() -> new RuntimeException("No profile found by user id: " + userId));
        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());
        order.setAddress(userProfile.getAddress());
        order.setCity(userProfile.getCity());
        order.setState(userProfile.getState());
        order.setZip(userProfile.getZip());
        order.setShippingAmount(FLAT_SHIPPING_RATE);
        return orderRepository.save(order);
    }
}
