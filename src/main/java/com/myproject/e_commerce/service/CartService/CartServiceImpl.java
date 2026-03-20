package com.myproject.e_commerce.service.CartService;
import com.myproject.e_commerce.constants.DiscountType;
import com.myproject.e_commerce.dao.InCartDAO.InCartDAO;
import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.CartResponseDTO;
import com.myproject.e_commerce.dto.InCartDTO;
import com.myproject.e_commerce.entity.*;
import com.myproject.e_commerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InCartDAO inCartDAO;
    private final PromotionRepository promotionRepository;
    public CartServiceImpl(PromotionRepository promotionRepository,CartRepository cartRepository, CartItemsRepository cartItemsRepository, UserRepository userRepository , ProductRepository productRepository,InCartDAO inCartDAO) {
        this.promotionRepository = promotionRepository;
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
    public CartResponseDTO getCart(String username,String code) {
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
        BigDecimal discountTotalPrice = BigDecimal.ZERO;
        Promotion promotion;
        if (code != null &&!code.trim().isEmpty()) {
            promotion = promotionRepository.findByName(code);
            if (promotion == null) {
                throw new RuntimeException("Mã không tồn tại");
            }
            LocalDate now = LocalDate.now();
            LocalDate expiredDate = promotion.getEndDate();
            if(expiredDate.isAfter(now)) {
                if (promotion != null && promotion.getDiscountType() == DiscountType.FIXED_AMOUNT) {
                    discountTotalPrice = promotion.getDiscountValue();
                } else {
                    BigDecimal tempNumber = cartTotalPrice.multiply(promotion.getDiscountValue());
                    discountTotalPrice = tempNumber.divide(new BigDecimal("100"), 2);
                }
                BigDecimal finalTotalPrice = cartTotalPrice.subtract(discountTotalPrice).max(BigDecimal.ZERO);
            }else {
                throw new RuntimeException("the promotion had expired");
            }
        }
        BigDecimal finalTotalPrice = cartTotalPrice.subtract(discountTotalPrice).max(BigDecimal.ZERO);
        return CartResponseDTO.builder()
                .items(inCartDTOList)
                .cartTotalPrice(cartTotalPrice)
                .discountTotalPrice(finalTotalPrice)
                .build();
    }
    @Transactional
    @Override
    public void deleteCartItem(Integer cartItemsId) {
        cartItemsRepository.deleteById(cartItemsId);
    }
}
