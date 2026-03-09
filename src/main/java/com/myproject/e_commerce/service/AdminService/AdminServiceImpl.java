package com.myproject.e_commerce.service.AdminService;

import com.myproject.e_commerce.dao.AdminDAO.AdminDAO;
import com.myproject.e_commerce.dao.OrderDAO.OrderDetailsDAO;
import com.myproject.e_commerce.dto.AdminDashboardDTO;
import com.myproject.e_commerce.entity.OrderDetails;
import com.myproject.e_commerce.repository.OrderDetailsRepository;
import com.myproject.e_commerce.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminDAO adminDAO;
    private final OrderDetailsRepository orderDetailsRepository;
    public  AdminServiceImpl(AdminDAO adminDAO,OrderDetailsRepository orderDetailsRepository) {
        this.adminDAO = adminDAO;
        this.orderDetailsRepository = orderDetailsRepository;
    }
    @Override
    public AdminDashboardDTO AdminDashboard() {
        long totalProducts = adminDAO.countAllProducts();
        long totalOrders = adminDAO.countAllOrders();
        long totalUsers = adminDAO.countAllUsers();
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        BigDecimal total = orderDetailsList.stream()
                .map(OrderDetails::getOriginalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        AdminDashboardDTO adminDashboardDTO = AdminDashboardDTO.builder()
                .totalOrders(totalOrders)
                .totalProducts(totalProducts)
                .totalUsers(totalUsers)
                .totalRevenue(total)
                .build();
        return adminDashboardDTO;
    }
}
