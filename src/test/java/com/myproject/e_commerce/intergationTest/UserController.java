package com.myproject.e_commerce.intergationTest;

import com.myproject.e_commerce.constants.DiscountType;
import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAO;
import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAOImp;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.entity.*;
import com.myproject.e_commerce.repository.*;
import com.myproject.e_commerce.service.CartService.CartService;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@TestPropertySource("/application_test.properties")
@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
public class UserController {
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
    @WithMockUser(username = "testuser",authorities = {"ROLE_CUSTOMER"})
    public void getCustomerDetailsByUsernameHttpRequest() throws Exception {
        CustomerDetailDTO customerDetailDTO = customerService.getCustomerDetailsByUsername("testuser");
        assertEquals("testuser", customerDetailDTO.getFullName());
        MvcResult mvcResult = this.mockMvc.perform(get("/cus/customerdetail"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "/user-profile/customer-details");
    }
    @Test
    @WithMockUser(username = "testuser",authorities = {"ROLE_CUSTOMER"})
    public void updateCustomerDetailsHttpRequest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/cus/update")
                        .param("username", "testuser1")
                        .param("email", "testuser@gmail.com")
                        .param("fullName", "testuser")
                        .param("phoneNumber", "099123123")
                        .param("address", "123123")
                )
                .andExpect(status().is3xxRedirection())
                .andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:/cus/customerdetail");
    }
    @Test
    @WithMockUser(username = "testuser",authorities = {"ROLE_CUSTOMER"})
    public void deleteCustomerByIdHttpRequest() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/cus/delete"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:/");
    }
    @Test
    @WithMockUser(username = "testuser",authorities = {"ROLE_CUSTOMER"})
    public void viewOrdersHttpRequest() throws Exception{
        MvcResult mvcResult = this.mockMvc.perform(get("/cus/orders"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "/user-profile/order-history");
    }
    @Test
    @WithMockUser(username = "testuser",authorities = {"ROLE_CUSTOMER"})
    public void viewOrderDetailsHttpRequest() throws Exception{
//        CustomerDetailDTO customerDetailDTO = CustomerDetailDTO.builder()
//                .fullName("testuser")
//                .email("testuser@gmail.com")
//                .address("123123")
//                .phoneNumber("099123123")
//                .build();
//        OrderDetailsWrapperDTO orderDetailsWrapperDTO = OrderDetailsWrapperDTO.builder()
//                .orderId(1)
//                .discountName("testdiscount")
//                .discountType(DiscountType.PERCENTAGE)
//                .grandTotalPrice(new BigDecimal(100))
//                .status(StatusOrder.PENDING)
//                .customerDetailDTO(customerDetailDTO)
//                .build();
//        when(orderService.getOrderDetails(1,"testuser")).thenReturn(orderDetailsWrapperDTO);
        MvcResult mvcResult = this.mockMvc.perform(get("/cus/orderdetails/{id}",1))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "/user-profile/order-history-details");
        List<OrderDTO> orderDTOS = orderService.getOrder("testuser");
        System.out.println(orderDTOS);
    }
    @Test
    @WithMockUser(username = "testuser",authorities = {"ROLE_CUSTOMER"})
    public void CancelOrderHttpRequest() throws Exception{
        MvcResult mvcResult = this.mockMvc.perform(post("/cus/cancelOrder")
                        .param("orderId", "1"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:/cus/orderdetails/{id}");
    }
}
