package com.sb.catalog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPricingDTO {

    private UUID productId;
    private BigDecimal msrp;
    private BigDecimal map;
    private BigDecimal cost;
    private BigDecimal sell;
    private BigDecimal base;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createdBy;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;
}

