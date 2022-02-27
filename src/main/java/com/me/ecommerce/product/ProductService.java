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

    List<ProductResponse> getAllByKeyword(String keyword) {
        List<ProductResponse> allProducts = new ArrayList<>();
        for (Product product : productRepository.findByNameContainingIgnoreCase(keyword)) {
            allProducts.add(new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getDescription()));
        }
        return allProducts;
    }

    public Optional<ProductResponse> getById(int id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            Product p = product.get();
            return Optional.of(new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getDescription()));
        } else {
            return Optional.empty();
        }
    }
}
