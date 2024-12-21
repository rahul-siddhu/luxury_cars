package com.rahul.product_inventory.models;

import com.rahul.product_inventory.models.enums.FuelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {

    @NotEmpty(message = "The car name is required")
    private String carName;

    @NotEmpty(message = "The model is required")
    private String model;

    @NotNull(message = "The year is required")
    @Min(value = 1886, message = "The year must be after 1885") // The first car was invented in 1886
    private Integer year;

    @Min(value = 0, message = "The price cannot be negative")
    private double price;

    @NotEmpty(message = "The color is required")
    private String color;

    @NotNull(message = "The fuel type is required")
    private FuelType fuelType; // Updated from String to FuelType

    @Size(min = 10, message = "The description should be at least 10 characters")
    @Size(max = 2000, message = "The description cannot exceed 2000 characters")
    private String description;

    private MultipartFile imageFile;
}
