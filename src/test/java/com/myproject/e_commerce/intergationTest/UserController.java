package com.myproject.e_commerce.intergationTest;

import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAO;
import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAOImp;
import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.CustomerRegistrationDTO;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.OrderService.OrderService;
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
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private OrderService orderService;
    @Autowired
    CustomerDAOImp customerDAO;
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
        customerService.save(customerRegistrationDTO);
//        jdbc.execute("INSERT INTO users (username, password, enabled) VALUES ('testuser', '123123', true)");
//        jdbc.execute("INSERT INTO customer_details (users_id, email, full_name, phone_number, address) VALUES ('testuser', 'testuser@gmail.com', 'testuser', '099123123', '123123')");
    }
    @AfterEach
    public void tearDown() {
        jdbc.execute("DELETE FROM authorities WHERE username = 'testuser'");
        jdbc.execute("DELETE FROM customer_details WHERE users_id = 'testuser'");
        jdbc.execute("DELETE FROM users WHERE username = 'testuser'");
        jdbc.execute("ALTER TABLE customer_details ALTER COLUMN id RESTART WITH 1");
        jdbc.execute("ALTER TABLE authorities ALTER COLUMN id RESTART WITH 1");
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

}
