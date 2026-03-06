package com.myproject.e_commerce.service.CartService;
import com.myproject.e_commerce.dao.InCartDAO.InCartDAO;
import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.CartResponseDTO;
import com.myproject.e_commerce.dto.InCartDTO;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartItemsRepository cartItemsRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private InCartDAO inCartDAO;
    public CartServiceImpl(CartRepository cartRepository, CartItemsRepository cartItemsRepository, UserRepository userRepository , ProductRepository productRepository,InCartDAO inCartDAO) {
        this.inCartDAO = inCartDAO;
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
    @Override
    public CartResponseDTO getCart(String username) {
        Cart cart = inCartDAO.getCart(username);
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return CartResponseDTO.builder()
                    .items(Collections.emptyList())
                    .cartTotalPrice(BigDecimal.ZERO)
                    .build();
        }
        Set<CartItems> cartItems = cart.getCartItems();
        List<InCartDTO> inCartDTOList = cartItems.stream()
                .map(cartItem -> InCartDTO.builder()
                        .cartItemsId(cartItem.getId())
                        .productName(cartItem.getProduct().getName())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .description(cartItem.getProduct().getDescription())
                        .imageUrl(cartItem.getProduct().getImageUrl())
                        .totalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .build())
                .toList();
        BigDecimal cartTotalPrice = inCartDTOList.stream()
                .map(InCartDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return CartResponseDTO.builder()
                .items(inCartDTOList)
                .cartTotalPrice(cartTotalPrice)
                .build();
    }
    @Transactional
    @Override
    public void deleteCartItem(Integer cartItemsId) {
        cartItemsRepository.deleteById(cartItemsId);
    }
}
