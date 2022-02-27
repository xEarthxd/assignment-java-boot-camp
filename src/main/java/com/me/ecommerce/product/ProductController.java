package com.me.ecommerce.product;

import com.me.ecommerce.product.message.ProductResponse;
import com.me.ecommerce.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value="/api/products")
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping(value="/api/products", params="keyword")
    public List<ProductResponse> getAllByKeyword(@RequestParam(name="keyword") String keyword) {
        return productService.getAllByKeyword(keyword);
    }

    @GetMapping(value="/api/products/{id}")
    public Optional<Product> getById(@PathVariable int id) {
        return productService.getById(id);
    }
}
