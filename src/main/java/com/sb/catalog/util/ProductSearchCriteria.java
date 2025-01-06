package com.sb.catalog.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria {
    private String productName;
    private String sku;
    private String categoryName;
    private String brandName;
}
