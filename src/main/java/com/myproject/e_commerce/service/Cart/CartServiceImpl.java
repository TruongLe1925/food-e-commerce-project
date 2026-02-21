package com.myproject.e_commerce.service.Cart;

import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.entity.Cart;
import com.myproject.e_commerce.entity.CartItems;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.entity.User;
import com.myproject.e_commerce.repository.CartItemsRepository;
import com.myproject.e_commerce.repository.CartRepository;
import com.myproject.e_commerce.repository.ProductRepository;
import com.myproject.e_commerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartItemsRepository cartItemsRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    public CartServiceImpl(CartRepository cartRepository, CartItemsRepository cartItemsRepository, UserRepository userRepository , ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    @Transactional
    @Override
    public void addCart(CartDTO cartDTO) {
        User user = userRepository.findByUsername(cartDTO.getUsername()).orElse(null);
        Cart cart = cartRepository.findByUser(user).orElse(null);
        Product product = productRepository.findByName(cartDTO.getProductName());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        Optional<CartItems> existingItem = cartItemsRepository.findByCartAndProduct(cart, product);
        if (existingItem.isPresent()) {
            CartItems item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartDTO.getQuantity());
            cartItemsRepository.save(item);
        } else {
            CartItems newItem = CartItems.builder()
                    .quantity(cartDTO.getQuantity())
                    .cart(cart)
                    .product(product)
                    .build();
            cartItemsRepository.save(newItem);
        }
    }
}
