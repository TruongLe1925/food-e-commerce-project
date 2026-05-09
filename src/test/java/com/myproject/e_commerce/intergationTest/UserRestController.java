package com.myproject.e_commerce.intergationTest;

import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dto.AddProductDTO;
import com.myproject.e_commerce.dto.CartDTO;
import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.entity.CustomerDetails;
import com.myproject.e_commerce.entity.Status;
import com.myproject.e_commerce.repository.StatusRepository;
import com.myproject.e_commerce.service.CartService.CartService;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application_test.properties")
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestController {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    public MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void setUp() {
        CustomerRegistrationDTO customerRegistrationDTO = CustomerRegistrationDTO.builder()
                .username("testuser")
                .email("testuser@gmail.com")
                .password("123123")
                .confirmPassword("123123")
                .address("123123")
                .fullName("testuser")
                .phoneNumber("099123123")
                .build();
        AddProductDTO addProductDTO = AddProductDTO.builder()
                .productName("testproduct")
                .description("testproduct")
                .price(new BigDecimal(100))
                .thumbnailUrl("testThumnailProduct")
                .imageUrl("testImageProduct")
                .quantity(100)
                .build();
        CartDTO cartDTO = CartDTO.builder()
                .quantity(2)
                .productName("testproduct")
                .username("testuser")
                .build();
        Status status = Status.builder()
                .status(StatusOrder.PENDING)
                .build();
        statusRepository.save(status);
        customerService.save(customerRegistrationDTO);
        productService.addProduct(addProductDTO);
        cartService.addCart(cartDTO);
        orderService.addToOrder("testuser",null,null,new BigDecimal(100),new BigDecimal(100));
    }
    @AfterEach
    public void tearDown() {
        jdbc.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbc.execute("TRUNCATE TABLE order_details RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE orders RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE cart_items RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE cart RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE status RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE product RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE authorities RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE customer_details RESTART IDENTITY");
        jdbc.execute("TRUNCATE TABLE users");
        jdbc.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
    @Test
    public void testCustomerDetailHttpRequest() throws Exception {
            mockMvc.perform(get("/api/user/customerdetail")
                            .with(jwt()
                                    .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                                    .jwt(j -> j.claim("sub", "testuser"))
                            )
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.fullName").value("testuser"))
                    .andExpect(jsonPath("$.email").value("testuser@gmail.com"));
    }
    @Test
    public void testHttpRequestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/customerdetail"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void testUpdateCustomerProfile() throws Exception {
        CustomerDetailDTO customerDetailDTO = new CustomerDetailDTO();
        customerDetailDTO.setFullName("testuser2");
        customerDetailDTO.setEmail("testuser@gmail.com2");
        mockMvc.perform(patch("/api/user/profile")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                                .jwt(j -> j.claim("sub", "testuser"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDetailDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("testuser2"))
                .andExpect(jsonPath("$.email").value("testuser@gmail.com2"));
    }

    @Test
    public void testGetUserOrders() throws Exception {
        mockMvc.perform(get("/api/orders/allOrders")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                                .jwt(j -> j.claim("sub", "testuser"))
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value("1"));
    }

    @Test
    public void testGetUserOrdersUnauthorized() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetOrderDetails() throws Exception {
        mockMvc.perform(get("/api/orders/{orderId}",1)
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                                .jwt(j -> j.claim("sub", "testuser"))
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("1"));
    }

    @Test
    public void testGetOrderDetailsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/orders/{orderId}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                                .jwt(j -> j.claim("sub", "testuser"))
                        )
                        .param("cartTotalPrice", "200")
                        .param("discountTotalPrice", "180")
                        .param("note", "test note")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateOrderUnauthorized() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .param("cartTotalPrice", "200")
                        .param("discountTotalPrice", "180")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCancelOrder() throws Exception {
        mockMvc.perform(put("/api/orders/1/cancel")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                                .jwt(j -> j.claim("sub", "testuser"))
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCancelOrderUnauthorized() throws Exception {
        mockMvc.perform(put("/api/orders/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
