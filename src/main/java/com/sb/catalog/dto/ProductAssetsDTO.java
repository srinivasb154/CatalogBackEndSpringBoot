package com.sb.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAssetsDTO {

    private Integer productAssetId;
    @JsonProperty("_id")
    private UUID productId;
    private String fileName;
    private String type;
    private String extension;
    private byte[] binaryData;
}
