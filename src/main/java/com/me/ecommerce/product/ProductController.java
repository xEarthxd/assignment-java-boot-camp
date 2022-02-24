package com.me.ecommerce.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/api/products")
    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }
}
