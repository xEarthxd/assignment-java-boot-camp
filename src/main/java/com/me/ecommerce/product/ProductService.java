package com.me.ecommerce.product;

import com.me.ecommerce.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

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
