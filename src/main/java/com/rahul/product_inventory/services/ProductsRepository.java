package com.rahul.product_inventory.services;

import com.rahul.product_inventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

//In extends type is datatype of id
public interface ProductsRepository extends JpaRepository<Product, Integer> {
}
