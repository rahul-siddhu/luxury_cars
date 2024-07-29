package com.rahul.product_inventory.models;

import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name= "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String brand;
    private String category;
    private double price;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Date createdAt;
    private String imageFileName;


}
