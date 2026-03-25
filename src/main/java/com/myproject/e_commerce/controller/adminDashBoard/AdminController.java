package com.myproject.e_commerce.controller.adminDashBoard;

import com.myproject.e_commerce.constants.DiscountType;
import com.myproject.e_commerce.constants.ProductStock;
import com.myproject.e_commerce.constants.Role;
import com.myproject.e_commerce.constants.StatusOrder;
import com.myproject.e_commerce.dto.*;
import com.myproject.e_commerce.entity.Authority;
import com.myproject.e_commerce.entity.Category;
import com.myproject.e_commerce.service.AdminService.AdminService;
import com.myproject.e_commerce.service.CategoryService.CategoryService;
import com.myproject.e_commerce.service.CustomerService.CustomerService;
import com.myproject.e_commerce.service.EmployeeService.EmployeeService;
import com.myproject.e_commerce.service.FileService;
import com.myproject.e_commerce.service.OrderService.OrderService;
import com.myproject.e_commerce.service.ProductService.ProductService;
import com.myproject.e_commerce.service.PromotionService.PromotionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final FileService fileService;
    private final AdminService adminService;
    private final PromotionService promotionService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    public AdminController(EmployeeService employeeService,CustomerService customerService,ProductService productService,FileService fileService,AdminService adminService,PromotionService promotionService,CategoryService categoryService) {
        this.employeeService=employeeService;
        this.customerService = customerService;
        this.productService = productService;
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
    @PostMapping("/changeBanner")
    public String changeBanner(@RequestParam("imageFile") MultipartFile image) {
        String banner = fileService.saveBanner(image);
        adminService.changeBanner(banner);
        return "redirect:/admin/dashboard";
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
        model.addAttribute("addCategory", new AddCategoryDTO());
        model.addAttribute("category", categoryDTOS);
        model.addAttribute("product", product);
        return  "admin/productdashboard/product";
    }
    @PostMapping("/addProduct")
    public String addProduct(
            @ModelAttribute("addProduct") AddProductDTO addProductDTO,
            @RequestParam(value = "fileThumbnail") MultipartFile thumbFile,
            @RequestParam(value = "fileImage") MultipartFile imgFile) {
        String thumbnailName = fileService.save(thumbFile);
        String imageName = fileService.save(imgFile);
        addProductDTO.setThumbnailUrl(thumbnailName);
        addProductDTO.setImageUrl(imageName);
        productService.addProduct(addProductDTO);
        return "redirect:/admin/product";
    }
    @PostMapping("/addCategory")
    public  String addCategory(@ModelAttribute("addCategory") AddCategoryDTO addCategoryDTO,
                               @RequestParam("fileThumbnailCat") MultipartFile thumbnailFile ) {
        String thumbnailName = fileService.saveCat(thumbnailFile);
        addCategoryDTO.setThumbnailUrl(thumbnailName);
        categoryService.addCategory(addCategoryDTO);
        return "redirect:/admin/product";
    }
    @GetMapping("/assignCategory/{id}")
    public String assignCategory(Model model,@PathVariable("id") Integer id){
        model.addAttribute("productId", id);
        model.addAttribute("allCategories",categoryService.findAll());
        return  "/admin/productdashboard/assignCategoryPage";
    }
    @PostMapping("/addProductToCat")
    public String addProductToCat(@RequestParam("productId") Integer productId,
                                  Model model,@RequestParam("categoryIds") List<Integer> categoryId) {
        categoryService.updateCategory(categoryId,productId);
        return  "redirect:/admin/product";
    }
    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam("id") Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return "redirect:/admin/product";
    }
    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Integer productId) {
        productService.deleteProductById(productId);
        return "redirect:/admin/product";
    }
    @GetMapping("/viewProductDetail/{id}")
    public String viewProductDetail(@PathVariable("id") Integer productId, Model model ) {
        ProductDashboardDTO productDashboardDTO = productService.findProductById(productId);
        model.addAttribute("product",productDashboardDTO);
        return "/admin/productdashboard/productDetail";
    }
    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable("id") Integer productId, Model model ) {
        ProductDashboardDTO productDashboardDTO = productService.findProductById(productId);
        model.addAttribute("id",productId);
        model.addAttribute("product",productDashboardDTO);
        return  "/admin/productdashboard/editProduct";
    }
    @PostMapping("/updateProduct")
    public  String updateProduct(@ModelAttribute("product") ProductDashboardDTO productDashboardDTO,
                                 @RequestParam("productId") Integer productId,
                                 @RequestParam(value = "thumbnail",required = false) MultipartFile thumbail,
                                 @RequestParam(value = "image",required = false) MultipartFile image) {
        if (!thumbail.isEmpty()) {
            String thumbnailName = fileService.save(thumbail);
            productDashboardDTO.setThumbnailUrl(thumbnailName);
        }
        if(!image.isEmpty()) {
            String imageName = fileService.save(image);
            productDashboardDTO.setImageUrl(imageName);
        }
        productService.updateProduct(productId, productDashboardDTO);
        return "redirect:/admin/product";
    }


    @Value("${page-size}")
    private int pageSize;
    @GetMapping("/customer")
    public String customer(Model model,@RequestParam(name = "keyword",required = false) String keyword,@RequestParam(name = "page", defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CustomerDetailDTO> customerPage;

        if (keyword != null && !keyword.isEmpty()) {
            customerPage = customerService.searchCustomer(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            customerPage = customerService.findAllCustomer(pageable);
        }

        AdminDashboardDTO adminDashboardDTO = adminService.AdminDashboard();
        model.addAttribute("adminDashboardDTO", adminDashboardDTO);
        model.addAttribute("cusPage", customerPage);
        model.addAttribute("cus", customerPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customerPage.getTotalPages());

        return "admin/customerdashboard/customer";
    }


    @PostMapping("/toggle-status")
    public String toggleStatus(@RequestParam("username") String username) {
        customerService.updateCustomerStatus(username);
        return "redirect:/admin/customer";
    }
    @GetMapping("/viewCustomerDetail/{id}")
    public String viewCustomerDetail(Model model,@PathVariable("id") Integer customerId) {
        CustomerDetailDTO customerDetailDTO = customerService.getCustomerDetails(customerId);
        model.addAttribute("customer",customerDetailDTO);
        return  "/admin/customerdashboard/viewCustomerDetail";
    }
    @GetMapping("/authority")
    public   String authority(Model model) {
        List<AuthorityDTO> authorities = adminService.findAllAuthorities();
        model.addAttribute("managers",authorities);
        model.addAttribute("employee",new AuthorityDTO());
        return  "admin/authoritydashboard/authority";
    }
    @PostMapping("/createEmployee")
    public String createEmployee(@ModelAttribute("employee") AuthorityDTO authorityDTO,@RequestParam("roles") Set<Role> roles ) {
        System.out.println("roles"+authorityDTO);
        employeeService.saveEmployee(authorityDTO,roles);
        return "redirect:/admin/authority";
    }
    @PostMapping("/deleteEmployee")
    public  String deleteEmployee(@RequestParam("username") String username) {
        employeeService.deleteEmployee(username);
        return "redirect:/admin/authority";
    }
    @GetMapping("/editEmployee/{username}")
    public String editEmployee(@PathVariable("username") String username, Model model) {
        AuthorityDTO authorityDTO = employeeService.getUserByUsername(username);
        AuthorityDTO authorityDTO1 = AuthorityDTO.builder()
                .username(username)
                .authorities(authorityDTO.getAuthorities())
                .build();
        model.addAttribute("employee",authorityDTO1);
        return  "admin/authoritydashboard/authorityEdit";
    }
    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute("employee") AuthorityDTO authorityDTO){
        employeeService.updateEmployee(authorityDTO.getUsername(), authorityDTO.getPassword());
        return "redirect:/admin/authority";
    }
    @PostMapping("/toggleEnable")
    public String updateEnable(@RequestParam("username") String username){
        customerService.updateCustomerStatus(username);
        return "redirect:/admin/authority";
    }
    @PostMapping("/toggleAdminRole")
    public String toggleAdminRole(@RequestParam("username") String username){
        employeeService.toggleAdminRole(username);
        return "redirect:/admin/authority";
    }


    @GetMapping("/promotion")
    public String promotion(Model model) {
        List<PromotionDTO> promotionDTOS = promotionService.findAllPromotion();
        model.addAttribute("promotion",promotionDTOS);
        model.addAttribute("createPromotion",new PromotionDTO());
        return  "admin/promotion";
    }
    @PostMapping("/savePromotion")
    public String savePromotion(@ModelAttribute("createPromotion") PromotionDTO promotionDTO) {
        promotionService.addPromotion(promotionDTO);
        return "redirect:/admin/promotion";
    }
}
