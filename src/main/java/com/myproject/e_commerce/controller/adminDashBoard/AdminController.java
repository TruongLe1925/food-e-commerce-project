package com.myproject.e_commerce.controller.adminDashBoard;

import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.entity.Authority;
import com.myproject.e_commerce.service.AdminService.AdminService;
import com.myproject.e_commerce.service.CategoryService.CategoryService;
import com.myproject.e_commerce.service.FileService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import com.myproject.e_commerce.service.PromotionService.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final FileService fileService;
    private final AdminService adminService;
    private final PromotionService promotionService;
    private final CategoryService categoryService;
    public AdminController(FileService fileService,AdminService adminService,PromotionService promotionService,CategoryService categoryService) {
        this.fileService = fileService;
        this.categoryService = categoryService;
        this.promotionService = promotionService;
        this.adminService = adminService;
    }
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        AdminDashboardDTO adminDashboardDTO = adminService.AdminDashboard();
        List<PromotionDTO> promotionDTOS = promotionService.findAllPromotion();
        model.addAttribute("promotion",promotionDTOS);
        model.addAttribute("adminDashboardDTO", adminDashboardDTO);
        return "admin/admin-dashboard";
    }
    @GetMapping("/product")
    public  String product(Model model,@RequestParam(name = "catId", required = false) Integer id,
                           @RequestParam(name = "status", required = false) ProductStock productStock,
                           @RequestParam(name = "keyword",required = false) String keyword) {
        List<CategoryDTO> categoryDTOS = categoryService.findAll();
        List<ProductDashboardDTO> product;
        if(id != null) {
            product = adminService.findAllProductsByCategory(id);
        } else if(productStock != null) {
            product = adminService.findProductsByStock(productStock);
        }else if(keyword != null) {
            product = adminService.searchProduct(keyword);
        } else{
            product = adminService.findAllProducts();
        }
        model.addAttribute("addProduct", new AddProductDTO());
        model.addAttribute("addCategory", new CategoryDTO());
        model.addAttribute("category", categoryDTOS);
        model.addAttribute("product", product);
        return  "admin/product";
    }
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("addProduct") AddProductDTO addProductDTO,@RequestParam("fileThumbnail") MultipartFile thumbFile,
                             @RequestParam("fileImage") MultipartFile imgFile ) {

        String thumbnailName = fileService.save(thumbFile);
        String imageName = fileService.save(imgFile);
        addProductDTO.setThumbnailUrl(thumbnailName);
        addProductDTO.setImageUrl(imageName);
        adminService.addProduct(addProductDTO);
        return "redirect:/admin/product";
    }
    @PostMapping("/addCategory")
    public  String addCategory(@ModelAttribute("addCategory") CategoryDTO addCategoryDTO) {

        return "redirect:/admin/product";
    }
    @GetMapping("/customer")
    public   String customer(Model model) {
        List<CustomerDetailDTO> customerDetailDTOS = adminService.findAllCustomer();
        AdminDashboardDTO adminDashboardDTO = adminService.AdminDashboard();
        model.addAttribute("adminDashboardDTO", adminDashboardDTO);
        model.addAttribute("cus",customerDetailDTOS);
        return  "admin/customer";
    }
    @GetMapping("/authority")
    public   String authority(Model model) {
        List<AuthorityDTO> authorities = adminService.findAllAuthorities();
        model.addAttribute("managers",authorities);
        return  "admin/authority";
    }
    @GetMapping("/promotion")
    public    String promotion(Model model) {
        List<PromotionDTO> promotionDTOS = promotionService.findAllPromotion();
        model.addAttribute("promotion",promotionDTOS);

        return  "admin/promotion";
    }
}
