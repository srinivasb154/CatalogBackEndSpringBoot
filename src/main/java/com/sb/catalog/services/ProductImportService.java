package com.sb.catalog.services;

import com.sb.catalog.models.Product;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductImportService {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductImportService(ProductService productService,
                                CategoryService categoryService,
                                BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    public void importProducts(MultipartFile file, String mode) throws Exception {
        // Parse the CSV file
        List<Map<String, String>> rows = parseCSV(file);

        // Validate and preprocess rows
        List<Product> products = rows.stream()
                .map(this::processRow)
                .toList();

        if ("replace".equalsIgnoreCase(mode)) {
            replaceProducts(products);
        } else if ("add".equalsIgnoreCase(mode)) {
            addProducts(products);
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }

    private List<Map<String, String>> parseCSV(MultipartFile file) throws Exception {
        List<Map<String, String>> rows = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setHeader() // Automatically use the first record as header
                    .setSkipHeaderRecord(false) // Process the first row as data
                    .setTrim(true) // Trim surrounding spaces
                    .setIgnoreEmptyLines(true) // Ignore empty lines
                    .setQuote('"') // Handle fields enclosed in quotes
                    .setEscape('\\') // Escape character for special characters
                    .build()
                    .parse(reader);

            for (CSVRecord record : records) {
                rows.add(record.toMap());
            }
        }

        return rows;
    }

    private Product processRow(Map<String, String> row) {
        Product product = new Product();
        product.setProductName(row.get("productName"));
        product.setSku(row.get("sku"));
        product.setShortDescription(row.get("shortDescription"));
        product.setLongDescription(row.get("longDescription"));
        product.setShippingNotes(row.get("shippingNotes"));
        product.setWarrantyInfo(row.get("warrantyInfo"));
        product.setVisibleToFrontEnd(Boolean.parseBoolean(row.get("visibleToFrontEnd")));
        product.setFeaturedProduct(Boolean.parseBoolean(row.get("featuredProduct")));

        // Set category
        String categoryName = row.get("categoryName");
        if (categoryName != null && !categoryName.isEmpty()) {
            categoryService.getCategoryByCategoryName(categoryName)
                    .ifPresentOrElse(
                            product::setCategoryId,
                            () -> product.setCategoryId(null)
                    );
        } else {
            product.setCategoryId(null);
        }

        // Set brand
        String brandName = row.get("brandName");
        if (brandName != null && !brandName.isEmpty()) {
            brandService.getBrandByBrandName(brandName)
                    .ifPresentOrElse(
                            product::setBrandId,
                            () -> product.setBrandId(null)
                    );
        } else {
            product.setBrandId(null);
        }

        return product;
    }

    private void replaceProducts(List<Product> products) {
        productService.deleteAllProducts();
        productService.saveAllProducts(products);
    }

    private void addProducts(List<Product> products) {
        productService.saveAllProducts(products);
    }
}
