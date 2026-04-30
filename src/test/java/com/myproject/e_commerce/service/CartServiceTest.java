package com.myproject.e_commerce.service;

import com.myproject.e_commerce.constants.DiscountType;
import com.myproject.e_commerce.dao.InCartDAO.InCartDAO;
import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.CartResponseDTO;
import com.myproject.e_commerce.entity.Cart;
import com.myproject.e_commerce.entity.CartItems;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.entity.Promotion;
import com.myproject.e_commerce.entity.User;
import com.myproject.e_commerce.exception.exception.ProductNotFoundException;
import com.myproject.e_commerce.exception.exception.PromotionNotFoundException;
import com.myproject.e_commerce.exception.exception.UserNotFoundException;
import com.myproject.e_commerce.repository.CartItemsRepository;
import com.myproject.e_commerce.repository.CartRepository;
import com.myproject.e_commerce.repository.ProductRepository;
import com.myproject.e_commerce.repository.PromotionRepository;
import com.myproject.e_commerce.repository.UserRepository;
import com.myproject.e_commerce.service.CartService.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemsRepository cartItemsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InCartDAO inCartDAO;

    @Mock
    private PromotionRepository promotionRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Product product;
    private Cart cart;
    private CartDTO cartDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");

        product = new Product();
        product.setId(1);
        product.setName("iPhone 15");
        product.setPrice(BigDecimal.valueOf(25000000));
        product.setDescription("Latest iPhone model");
        product.setImageUrl("iphone15.jpg");

        cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        cartDTO = new CartDTO();
        cartDTO.setUsername("testuser");
        cartDTO.setProductName("iPhone 15");
        cartDTO.setQuantity(2);
    }

    @Test
    @DisplayName("Add product to new cart - Create new cart")
    void addCart_NewCart_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(productRepository.findByName("iPhone 15")).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartItemsRepository.findByCartAndProduct(any(Cart.class), eq(product))).thenReturn(Optional.empty());

        // When
        cartService.addCart(cartDTO);

        // Then
        verify(cartRepository).save(any(Cart.class));
        verify(cartItemsRepository).save(any(CartItems.class));
    }

    @Test
    @DisplayName("Add product to existing cart - Update quantity")
    void addCart_ExistingCartItem_UpdateQuantity() {
        // Given
        CartItems existingItem = new CartItems();
        existingItem.setId(1);
        existingItem.setCart(cart);
        existingItem.setProduct(product);
        existingItem.setQuantity(3);
        cart.getCartItems().add(existingItem);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findByName("iPhone 15")).thenReturn(product);
        when(cartItemsRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.of(existingItem));

        // When
        cartService.addCart(cartDTO);

        // Then
        assertEquals(5, existingItem.getQuantity(), "Quantity should be added");
        verify(cartItemsRepository).save(existingItem);
    }

    @Test
    @DisplayName("Add to cart failed - User not found")
    void addCart_UserNotFound_ThrowException() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> cartService.addCart(cartDTO));
    }

    @Test
    @DisplayName("Add to cart failed - Product not found")
    void addCart_ProductNotFound_ThrowException() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findByName("iPhone 15")).thenReturn(null);

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> cartService.addCart(cartDTO));
    }

    @Test
    @DisplayName("Get empty cart - Return zero value")
    void getCart_EmptyCart_ReturnEmptyResponse() {
        // Given
        when(inCartDAO.getCart("testuser")).thenReturn(null);

        // When
        CartResponseDTO result = cartService.getCart("testuser", null);

        // Then
        assertTrue(result.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, result.getCartTotalPrice());
    }

    @Test
    @DisplayName("Get cart with items - Calculate total correctly")
    void getCart_WithItems_ReturnCorrectTotal() {
        // Given
        CartItems item1 = new CartItems();
        item1.setId(1);
        item1.setProduct(product);
        item1.setQuantity(2);
        item1.setCart(cart);
        cart.getCartItems().add(item1);

        when(inCartDAO.getCart("testuser")).thenReturn(cart);

        // When
        CartResponseDTO result = cartService.getCart("testuser", null);

        // Then
        assertEquals(1, result.getItems().size());
        assertEquals(BigDecimal.valueOf(50000000), result.getCartTotalPrice());
    }

    @Test
    @DisplayName("Apply FIXED_AMOUNT promotion successfully")
    void getCart_WithValidFixedPromotion_ReturnDiscountedPrice() {
        // Given
        CartItems item1 = new CartItems();
        item1.setId(1);
        item1.setProduct(product);
        item1.setQuantity(2);
        item1.setCart(cart);
        cart.getCartItems().add(item1);

        Promotion promotion = new Promotion();
        promotion.setName("GIAM50K");
        promotion.setDiscountType(DiscountType.FIXED_AMOUNT);
        promotion.setDiscountValue(BigDecimal.valueOf(50000));
        promotion.setEndDate(LocalDate.now().plusDays(1));

        when(inCartDAO.getCart("testuser")).thenReturn(cart);
        when(promotionRepository.findByName("GIAM50K")).thenReturn(promotion);

        // When
        CartResponseDTO result = cartService.getCart("testuser", "GIAM50K");

        // Then
        assertEquals(BigDecimal.valueOf(49950000), result.getDiscountTotalPrice());
    }

    @Test
    @DisplayName("Apply PERCENTAGE promotion successfully")
    void getCart_WithValidPercentagePromotion_ReturnDiscountedPrice() {
        // Given
        CartItems item1 = new CartItems();
        item1.setId(1);
        item1.setProduct(product);
        item1.setQuantity(2);
        item1.setCart(cart);
        cart.getCartItems().add(item1);

        Promotion promotion = new Promotion();
        promotion.setName("GIAM10");
        promotion.setDiscountType(DiscountType.PERCENTAGE);
        promotion.setDiscountValue(BigDecimal.valueOf(10));
        promotion.setEndDate(LocalDate.now().plusDays(1));

        when(inCartDAO.getCart("testuser")).thenReturn(cart);
        when(promotionRepository.findByName("GIAM10")).thenReturn(promotion);

        // When
        CartResponseDTO result = cartService.getCart("testuser", "GIAM10");

        // Then: 50.000.000 - 10% = 45.000.000
        assertEquals(BigDecimal.valueOf(45000000.00).setScale(2), result.getDiscountTotalPrice().setScale(2));
    }

    @Test
    @DisplayName("Apply promotion failed - Code not found")
    void getCart_InvalidPromotion_ThrowException() {
        // Given
        CartItems item1 = new CartItems();
        item1.setId(1);
        item1.setProduct(product);
        item1.setQuantity(2);
        item1.setCart(cart);
        cart.getCartItems().add(item1);

        when(inCartDAO.getCart("testuser")).thenReturn(cart);
        when(promotionRepository.findByName("INVALID")).thenReturn(null);

        // When & Then
        assertThrows(PromotionNotFoundException.class, () -> cartService.getCart("testuser", "INVALID"));
    }

    @Test
    @DisplayName("Apply promotion failed - Code expired")
    void getCart_ExpiredPromotion_ThrowException() {
        // Given
        CartItems item1 = new CartItems();
        item1.setId(1);
        item1.setProduct(product);
        item1.setQuantity(2);
        item1.setCart(cart);
        cart.getCartItems().add(item1);

        Promotion promotion = new Promotion();
        promotion.setName("EXPIRED");
        promotion.setDiscountType(DiscountType.FIXED_AMOUNT);
        promotion.setDiscountValue(BigDecimal.valueOf(50000));
        promotion.setEndDate(LocalDate.now().minusDays(1));

        when(inCartDAO.getCart("testuser")).thenReturn(cart);
        when(promotionRepository.findByName("EXPIRED")).thenReturn(promotion);

        // When & Then
        assertThrows(PromotionNotFoundException.class, () -> cartService.getCart("testuser", "EXPIRED"));
    }

    @Test
    @DisplayName("Delete cart item successfully")
    void deleteCartItem_Success() {
        // When
        cartService.deleteCartItem(1);

        // Then
        verify(cartItemsRepository).deleteById(1);
    }

    @Test
    @DisplayName("Discount cannot exceed total - Final price minimum is 0")
    void getCart_DiscountExceedsTotal_ReturnZeroMinimum() {
        // Given
        Product cheapProduct = new Product();
        cheapProduct.setId(2);
        cheapProduct.setName("Cheap Item");
        cheapProduct.setPrice(BigDecimal.valueOf(10000));

        CartItems item1 = new CartItems();
        item1.setId(1);
        item1.setProduct(cheapProduct);
        item1.setQuantity(1);
        item1.setCart(cart);
        cart.getCartItems().add(item1);

        Promotion promotion = new Promotion();
        promotion.setName("GIAM200K");
        promotion.setDiscountType(DiscountType.FIXED_AMOUNT);
        promotion.setDiscountValue(BigDecimal.valueOf(200000));
        promotion.setEndDate(LocalDate.now().plusDays(1));

        when(inCartDAO.getCart("testuser")).thenReturn(cart);
        when(promotionRepository.findByName("GIAM200K")).thenReturn(promotion);

        // When
        CartResponseDTO result = cartService.getCart("testuser", "GIAM200K");

        // Then
        assertEquals(BigDecimal.ZERO, result.getDiscountTotalPrice());
    }
}
