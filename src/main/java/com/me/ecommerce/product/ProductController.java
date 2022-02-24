package com.me.ecommerce.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductService productService;

    @GetMapping(value="/api/products")
    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }

    @GetMapping(value="/api/products", params="keyword")
    public List<Product> getAllByKeyword(@RequestParam(name="keyword") String keyword) {
        return productService.getAllByKeyword(keyword);
    }
}
