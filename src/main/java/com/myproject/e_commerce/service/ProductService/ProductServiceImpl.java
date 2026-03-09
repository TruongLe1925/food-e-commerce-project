package com.myproject.e_commerce.service.ProductService;

import com.myproject.e_commerce.dto.ProductHomePageDTO;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public List<ProductHomePageDTO> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> ProductHomePageDTO.builder()
                        .productName(product.getName())
                        .productPrice(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .build())
                .toList();
    }
}
