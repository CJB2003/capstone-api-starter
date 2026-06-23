package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;

@Service
public class ShoppingCartService
{
    // a shopping cart is built from cart rows plus a product lookup for each row
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }

    public ShoppingCart getByUserId(int userId)
    {
        // load the user's cart rows, look up each product, and build the ShoppingCart
        List<CartItem> userCartItems = shoppingCartRepository.findByUserId(userId);
        ShoppingCart shoppingCart = new ShoppingCart();

        userCartItems.stream()
                .map(cartItem -> {
                    Product product = productService.getById(cartItem.getProductId());
                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                    shoppingCartItem.setProduct(product);
                    shoppingCartItem.setQuantity(cartItem.getQuantity());
                    return shoppingCartItem;
                })
                .forEach(shoppingCart::add);

        return shoppingCart;
    }

    // add additional methods here
    public ShoppingCart addProduct(int userId, int productId) {

        CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);
        // Checks if item is null, creates new cart item if null,
        // otherwise the quantity of existing item increments by 1
        if (cartItem == null) {
            CartItem newCartItem = new CartItem();
            newCartItem.setUserId(userId);
            newCartItem.setProductId(productId);
            newCartItem.setQuantity(1);
            shoppingCartRepository.save(newCartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            shoppingCartRepository.save(cartItem);
        }
        return getByUserId(userId);
    }
}
