package com.sb.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationsDTO {

    @JsonProperty("_id")
    private UUID productId;
    private String weight;
    private String color;
    private String dimensions;
    private String capacity;
    private String material;
    private String origin;
    private String size;
    private String wattage;
    private String voltage;
    private String specialFeatures;
}
