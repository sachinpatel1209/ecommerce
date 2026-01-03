package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartItemRepository itemRepository;

    private final ProductRepository  productRepository;

    private final UserRepository userRepository;


    public boolean addToCart(String userId, CartItemRequest request) {
        // look for the product
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) {
            return false;
        }
        Product  product = productOpt.get();
        if (product.getStockQuantity() < request.getQuantity()) {
            return false;
        }
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()){
            return false;
        }
        User user = userOpt.get();

        CartItem existingCartItem = itemRepository.findByUserAndProduct(user, product);
        if (existingCartItem != null) {
            // update quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            itemRepository.save(existingCartItem);
        } else {
            // create the new cart
            CartItem  cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            itemRepository.save(cartItem);
        }
        return true;
    }
}







