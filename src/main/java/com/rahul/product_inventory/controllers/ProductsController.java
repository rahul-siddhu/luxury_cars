package com.rahul.product_inventory.controllers;

import com.rahul.product_inventory.models.Product;
import com.rahul.product_inventory.models.ProductDto;
import com.rahul.product_inventory.services.ProductService;
import com.rahul.product_inventory.services.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    // Show product list with pagination and sorting
    @GetMapping({"", "/"})
    public String showProductList(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                  @RequestParam(value = "direction", defaultValue = "desc") String direction,
                                  Model model) {
        Page<Product> productPage = repo.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "products/index";
    }

    // Search products with pagination and sorting
    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "query", required = false) String query,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                 @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                 @RequestParam(value = "direction", defaultValue = "desc") String direction,
                                 Model model) {

        // Ensure page starts at 0, not 1 (pagination in Spring starts at 0)
        if (page <= 0) {
            page = 1;
        }

        // Print the debugging message
        System.out.println("Searching with query: " + query);
        System.out.println("Current Page: " + page);

        // If query is empty or null, display a message and avoid searching
//        if (query == null || query.trim().isEmpty()) {
//            model.addAttribute("products", Collections.emptyList());
//            model.addAttribute("query", "");
//            model.addAttribute("message", "Please enter a search term.");
//        } else {
//            // Fetch paginated and sorted results from the service
//        }
            Page<Product> productPage = productService.searchProducts(query, page - 1, size, sortBy, direction);

            // Add attributes for pagination and sorting to the model
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("query", query);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("totalItems", productPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("direction", direction);

        // Return to the index page
        return "products/index";
    }



    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto,
                                BindingResult result) {
        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "Image file is required"));
        }

        if (result.hasErrors()) {
            return "products/CreateProduct";
        }

        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Product product = new Product();
        product.setCarName(productDto.getCarName());
        product.setModel(productDto.getModel());
        product.setYear(productDto.getYear());
        product.setPrice(productDto.getPrice());
        product.setColor(productDto.getColor());
        product.setFuelType(productDto.getFuelType()); // Use enum directly
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        repo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/view")
    public String viewProduct(@RequestParam int id, Model model) {
        try {
            Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
            model.addAttribute("product", product);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/products";
        }
        return "products/ViewProduct";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {
        try {
            Product product = repo.findById(id).orElseThrow();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setCarName(product.getCarName());
            productDto.setModel(product.getModel());
            productDto.setYear(product.getYear());
            productDto.setPrice(product.getPrice());
            productDto.setColor(product.getColor());
            productDto.setFuelType(product.getFuelType()); // Map enum directly
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto", productDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id,
                                @Valid @ModelAttribute ProductDto productDto,
                                BindingResult result) {
        try {
            Product product = repo.findById(id).orElseThrow();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/EditProduct";
            }

            if (!productDto.getImageFile().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }

                product.setImageFileName(storageFileName);
            }

            product.setCarName(productDto.getCarName());
            product.setModel(productDto.getModel());
            product.setYear(productDto.getYear());
            product.setPrice(productDto.getPrice());
            product.setColor(productDto.getColor());
            product.setFuelType(productDto.getFuelType()); // Map enum directly
            product.setDescription(productDto.getDescription());

            repo.save(product);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {
        try {
            Product product = repo.findById(id).orElseThrow();

            Path imagePath = Paths.get("public/images/" + product.getImageFileName());
            try {
                Files.delete(imagePath);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }

            repo.delete(product);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }
}
