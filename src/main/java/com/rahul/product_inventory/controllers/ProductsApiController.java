package com.rahul.product_inventory.controllers;

import com.rahul.product_inventory.models.Product;
import com.rahul.product_inventory.models.ProductDto;
import com.rahul.product_inventory.services.ProductService;
import com.rahul.product_inventory.services.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsApiController {

    @Autowired
    private ProductsRepository repo;

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = repo.findAll();
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        return ResponseEntity.ok(product);
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException("Invalid product data");
        }

        Product product = new Product();
        product.setCarName(productDto.getCarName());
        product.setModel(productDto.getModel());
        product.setYear(productDto.getYear());
        product.setPrice(productDto.getPrice());
        product.setColor(productDto.getColor());
        product.setFuelType(productDto.getFuelType());
        product.setDescription(productDto.getDescription());

        Product savedProduct = repo.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @Valid @RequestBody ProductDto productDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalArgumentException("Invalid product data");
        }

        Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        product.setCarName(productDto.getCarName());
        product.setModel(productDto.getModel());
        product.setYear(productDto.getYear());
        product.setPrice(productDto.getPrice());
        product.setColor(productDto.getColor());
        product.setFuelType(productDto.getFuelType());
        product.setDescription(productDto.getDescription());

        Product updatedProduct = repo.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        repo.delete(product);
        return ResponseEntity.noContent().build();
    }
}
