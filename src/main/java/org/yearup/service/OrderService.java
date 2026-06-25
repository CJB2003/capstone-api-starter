package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;

@Service
public class OrderService {

    /*
    I made a decision to pass in profile service because the fields between profile and order model are similar.
    Both contain the key info of the user so why duplicate information in the db when there's already
    data for those fields in profile.
     */
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



}
