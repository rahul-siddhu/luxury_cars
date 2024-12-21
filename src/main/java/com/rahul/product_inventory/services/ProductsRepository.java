package com.rahul.product_inventory.services;

import com.rahul.product_inventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Product, Integer> {

    // Method for searching products based on multiple fields, with pagination
    Page<Product> findByCarNameContainingIgnoreCaseOrModelContainingIgnoreCaseOrColorContainingIgnoreCase(
            String carName, String model, String color, Pageable pageable);
}
