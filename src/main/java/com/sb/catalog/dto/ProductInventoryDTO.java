package com.sb.catalog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventoryDTO {

    private UUID productId;
    private String bin;
    private String location;
    private String source;
    private Integer onHand;
    private Integer onHold;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;
}

