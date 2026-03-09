package com.myproject.e_commerce.controller.home;

import com.myproject.e_commerce.dto.CustomerDetailDTO;
import com.myproject.e_commerce.dto.OrderDTO;
import com.myproject.e_commerce.dto.OrderDetailsWrapperDTO;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cus")
public class UserPage {
    private final CustomerService customerService;
    private final OrderService orderService;
    public UserPage(CustomerService customerService, OrderService orderService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }
    @GetMapping("/customerdetail")
    public String userDetails(Model model, Principal principal){
        String username = principal.getName();
        CustomerDetailDTO customerDetailDTO = customerService.getCustomerDetailsByUsername(username);
        model.addAttribute("user", customerDetailDTO);
        return "/user-profile/customer-details";
    }
    @PostMapping("/update")
    public String updateCustomerDetails(CustomerDetailDTO customerDetailDTO, Principal principal){
        String username = principal.getName();
        customerService.updateCustomerDetails(username,customerDetailDTO);
        return "redirect:/cus/customerdetail";
    }
    @PostMapping("/delete")
    public String deleteCustomer(Principal principal, HttpServletRequest request) throws ServletException{
        String username = principal.getName();
        customerService.deleteCustomerById(username);
        request.logout();
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    @GetMapping("/orders")
    public String viewOrders(Model model, Principal principal) {
        String username = principal.getName();
        List<OrderDTO> orderDTO = orderService.getOrder(username);
        model.addAttribute("orders", orderDTO);
        return "/user-profile/order-history";
    }
    @GetMapping("/orderdetails/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id , Model model, Principal principal) {
        String username = principal.getName();
        OrderDetailsWrapperDTO orderDetailsWrapperDTO = orderService.getOrderDetails(id, username);
        model.addAttribute("orderDetails", orderDetailsWrapperDTO);
        return "/user-profile/order-history-details";
    }
    @PostMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderId") Integer orderId, RedirectAttributes redirectAttributes) {
        orderService.cancelOrder(orderId);
        redirectAttributes.addAttribute("id", orderId);
        return "redirect:/cus/orderdetails/{id}";
    }
}
