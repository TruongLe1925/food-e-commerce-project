package com.myproject.e_commerce.service.ProductService;

import com.myproject.e_commerce.dao.ProductDAO.ProductDAO;
import com.myproject.e_commerce.dto.AddProductDTO;
import com.myproject.e_commerce.dto.CategoryDTO;
import com.myproject.e_commerce.dto.ProductDashboardDTO;
import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.entity.Category;
import com.myproject.e_commerce.entity.OrderDetails;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.repository.CategoryRepository;
import com.myproject.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductDAO productDAO;
    private  final CategoryRepository categoryRepository;
    public ProductServiceImpl(CategoryRepository categoryRepository ,ProductRepository productRepository,ProductDAO productDAO) {
        this.productRepository = productRepository;
        this.productDAO = productDAO;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<ProductHomePageDTO> findAllProducts() {
        List<Product> products = productDAO.getProducts();
        return products.stream().map(product -> ProductHomePageDTO.builder()
                        .productName(product.getName())
                        .productPrice(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .build())
                .toList();
    }
    @Transactional
    @Override
    public void deleteProductById(Integer productId) {
        Product product = productDAO.getProductById(productId);
        List<Category> category = product.getCategories();
        List<OrderDetails> orderDetails = product.getOrderDetails();
        for (Category categories : category) {
            categories.getProducts().remove(product);
        }
        for (OrderDetails orderDetail : orderDetails) {
            orderDetail.setProduct(null);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public ProductDashboardDTO findProductById(Integer productId) {
        Product product = productDAO.getProductById(productId);
        List<OrderDetails> orderDetails = product.getOrderDetails();
        BigDecimal grossIncome = orderDetails.stream()
                .map(OrderDetails::getDiscountPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<CategoryDTO> categoryDTOS = product.getCategories().stream().map(cat -> CategoryDTO.builder()
                .id(cat.getId())
                .name(cat.getName())
                .build()).toList();
        ProductDashboardDTO productDashboardDTO = ProductDashboardDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .categories(categoryDTOS)
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .thumbnailUrl(product.getThumbnailUrl())
                .createdDate(product.getCreateDate())
                .description(product.getDescription())
                .grossIncome(grossIncome)
                .build();
        return productDashboardDTO;
    }
    @Transactional
    @Override
    public void updateProduct(Integer productId,ProductDashboardDTO productDashboardDTO) {
        Product product = productRepository.findById(productId).orElse(null);
        product.setDescription(productDashboardDTO.getDescription());
        product.setPrice(productDashboardDTO.getPrice());
        product.setStock(productDashboardDTO.getStock());
        product.setName(productDashboardDTO.getProductName());
        if(productDashboardDTO.getThumbnailUrl() != null) {
            product.setThumbnailUrl(productDashboardDTO.getThumbnailUrl());
        }
        if(productDashboardDTO.getImageUrl() != null) {
            product.setImageUrl(productDashboardDTO.getImageUrl());
        }
        productRepository.save(product);
    }
    @Transactional
    @Override
    public void addProduct(AddProductDTO addProductDTO) {
        Product product = Product.builder()
                .name(addProductDTO.getProductName())
                .price(addProductDTO.getPrice())
                .stock(addProductDTO.getQuantity())
                .imageUrl(addProductDTO.getImageUrl())
                .thumbnailUrl(addProductDTO.getThumbnailUrl())
                .build();
        List<Integer> categories = addProductDTO.getCategories();
        if (categories != null && !categories.isEmpty()) {
            List<Category> category = categoryRepository.findAllById(categories);
            for (Category cat : category) {
                product.addCategory(cat);
            }
        }
        productRepository.save(product);
    }
}
