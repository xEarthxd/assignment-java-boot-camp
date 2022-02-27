package com.me.ecommerce.product;

import com.me.ecommerce.product.message.ProductResponse;
import com.me.ecommerce.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> getAllProduct() {
        List<ProductResponse> allProducts = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            allProducts.add(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getDescription()));
        }
        return allProducts;
    }

    public void setRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    List<Product> getAllByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Optional<Product> getById(int id) {
        return productRepository.findById(id);
    }
}
