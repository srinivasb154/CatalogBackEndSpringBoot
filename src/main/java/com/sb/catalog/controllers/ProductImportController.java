package com.sb.catalog.controllers;

import com.sb.catalog.services.ProductImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products/import")
public class ProductImportController {

    private final ProductImportService productImportService;

    public ProductImportController(ProductImportService productImportService) {
        this.productImportService = productImportService;
    }

    @PostMapping
    public ResponseEntity<String> importProducts(
            @RequestParam("file") MultipartFile file,
            @RequestParam("mode") String mode) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded. Please upload a CSV file.");
            }

            productImportService.importProducts(file, mode);

            return ResponseEntity.ok("Products imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error importing products: " + e.getMessage());
        }
    }
}
