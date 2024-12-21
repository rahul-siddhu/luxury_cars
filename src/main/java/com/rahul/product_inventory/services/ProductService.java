package com.rahul.product_inventory.services;
import com.rahul.product_inventory.models.Product;
import com.rahul.product_inventory.services.ProductsRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductsRepository productsRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    // Method to get all products
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    // Method to search products by name, model, year or color
//    public Page<Product> searchProducts(String query, int page, int size, String sortBy, String direction) {
//        // Create the Sort object for sorting by the specified field and direction
//        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//
//        // Create the Pageable object
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        // Perform the search query with pagination and sorting
//        return productsRepository.findByCarNameContainingIgnoreCaseOrModelContainingIgnoreCaseOrColorContainingIgnoreCase(
//                query, query, query, pageable);
//    }

    public Page<Product> searchProducts(String query, int page, int size, String sortBy, String direction) {
        // Create Pageable object with sorting direction and page size
        Pageable pageable = PageRequest.of(page, size);
        if (direction.equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        }

        // If no query is provided, return all products sorted
        if (query == null || query.isEmpty()) {
            return productsRepository.findAll(pageable);
        } else {
            // If query is provided, search by car name, model, or color
            return productsRepository.findByCarNameContainingIgnoreCaseOrModelContainingIgnoreCaseOrColorContainingIgnoreCase(query, query, query, pageable);
        }
    }
}
