package com.myproject.e_commerce.service.AdminService;

import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dao.AdminDAO.AdminDAO;
import com.myproject.e_commerce.dao.CustomerDAO.CustomerDAO;
import com.myproject.e_commerce.dao.OrderDAO.OrderDetailsDAO;
import com.myproject.e_commerce.dao.ProductDAO.ProductDAO;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.entity.*;
import com.myproject.e_commerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminDAO adminDAO;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;
    private final Admin admin;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BannerRepository bannerRepository;
    public  AdminServiceImpl(BannerRepository bannerRepository,OrdersRepository ordersRepository,CategoryRepository categoryRepository,ProductRepository productRepository,CustomerDAO customerDAO,AdminDAO adminDAO,OrderDetailsRepository orderDetailsRepository,ProductDAO productDAO,Admin admin) {
        this.bannerRepository = bannerRepository;
        this.ordersRepository = ordersRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.admin = admin;
        this.adminDAO = adminDAO;
        this.orderDetailsRepository = orderDetailsRepository;
        this.productDAO = productDAO;
        this.customerDAO = customerDAO;
    }
    @Override
    public AdminDashboardDTO AdminDashboard() {
        long totalProducts = adminDAO.countAllProducts();
        long totalOrders = adminDAO.countAllOrders();
        long totalUsers = adminDAO.countAllUsers();
        List<Orders> orders = ordersRepository.findAll();
        BigDecimal total = orders.stream()
                .filter(or -> or.getStatus().getStatus() == StatusOrder.COMPLETED)
                .map(Orders::getDiscountPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        AdminDashboardDTO adminDashboardDTO = AdminDashboardDTO.builder()
                .totalOrders(totalOrders)
                .totalProducts(totalProducts)
                .totalUsers(totalUsers)
                .totalRevenue(total)
                .build();
        return adminDashboardDTO;
    }

    @Override
    public List<ProductDashboardDTO> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return admin.getProducts(products);
    }


    @Override
    public List<AuthorityDTO> findAllAuthorities() {
        List<User> users = adminDAO.getAllUser();
        return users.stream().map(user-> AuthorityDTO.builder()
                .authorities(user.getAuthorities())
                .username(user.getUsername())
                .enable(user.isEnabled() == true?"active":"inactive")
                .build())
                .toList();
    }

    @Override
    public List<ProductDashboardDTO> findAllProductsByCategory(Integer id) {
        List<Product> products = productDAO.getProductsByCategory(id);
        return admin.getProducts(products);
    }

    @Override
    public List<ProductDashboardDTO> findProductsByStock(ProductStock productStock) {
        List<Product> products;
        if (productStock == ProductStock.OUT_OF_STOCK){
            products = productDAO.getProductsByInStock();
        }else {
            products = productDAO.getProductsByOutOfStock();
        }
        return admin.getProducts(products);
    }

    @Override
    public List<ProductDashboardDTO> searchProduct(String keyword) {
        String lowercase = keyword.toLowerCase();
        List<Product> products = productDAO.SearchProduct(lowercase);
        return admin.getProducts(products);
    }
    @Transactional
    @Override
    public void changeBanner(String imageUrl) {
        bannerRepository.deleteAll();
        Banner changeBanner = Banner.builder()
                .imageUrl(imageUrl)
                .build();
        bannerRepository.save(changeBanner);
    }

    @Override
    public BannerDTO banner() {
        Banner banner = bannerRepository.findFirstByOrderByIdAsc();
        BannerDTO bannerDTO = BannerDTO.builder()
                .imageUrl(banner.getImageUrl())
                .build();
        return bannerDTO;
    }
}
