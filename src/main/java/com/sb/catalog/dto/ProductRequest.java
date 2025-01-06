package com.sb.catalog.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ProductRequest {
    @JsonProperty("_id")
    private UUID productId;
    private String productName;
    private String sku;
    private String shortDescription;
    private String longDescription;
    private String shippingNotes;
    private String warrantyInfo;
    private boolean visibleToFrontEnd;
    private boolean featuredProduct;
    private UUID categoryId;
    private UUID brandId;
    private ProductSpecificationsDTO specifications;
    private List<ProductAssetsDTO> assets;
}

