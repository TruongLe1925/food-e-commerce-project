package com.myproject.e_commerce.controller.adminDashBoard;

import com.myproject.e_commerce.dto.AdminDashboardDTO;
import com.myproject.e_commerce.dto.OrderProcessDTO;
import com.myproject.e_commerce.service.AdminService.AdminService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        AdminDashboardDTO adminDashboardDTO = adminService.AdminDashboard();
        model.addAttribute("adminDashboardDTO", adminDashboardDTO);
        return "admin/admin-dashboard";
    }
    @GetMapping("/product")
    public  String product(Model model) {
        return  "admin/product";
    }
    @GetMapping("/customer")
    public   String customer(Model model) {
        return  "admin/customer";
    }
    @GetMapping("/authority")
    public   String authority(Model model) {
        return  "admin/authority";
    }
    @GetMapping("/promotion")
    public    String promotion(Model model) {
        return  "admin/promotion";
    }
    @GetMapping("/order")
    public   String orders(Model model) {
        return  "admin/orders";
    }
}
