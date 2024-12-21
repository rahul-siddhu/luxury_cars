package com.rahul.product_inventory.models;

import jakarta.persistence.*;
import lombok.Data;
import com.rahul.product_inventory.models.enums.FuelType;
import java.util.Date;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String carName;
    private String model;
    private int year; // Year of manufacture
    private double price;
    private String color;

    @Enumerated(EnumType.STRING) // To store fuel type as a string in the database
    private FuelType fuelType;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Date createdAt;
    private String imageFileName;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

//    public enum FuelType {
//        PETROL, DIESEL, ELECTRIC, HYBRID, CNG
//    }
}
