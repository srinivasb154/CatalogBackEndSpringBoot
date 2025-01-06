package com.sb.catalog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_pricing")
public class ProductPricing {

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "msrp", precision = 10, scale = 4)
    private BigDecimal msrp;

    @Column(name = "map", precision = 10, scale = 4)
    private BigDecimal map;

    @Column(name = "cost", precision = 10, scale = 4)
    private BigDecimal cost;

    @Column(name = "sell", precision = 10, scale = 4)
    private BigDecimal sell;

    @Column(name = "base", precision = 10, scale = 4)
    private BigDecimal base;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;
}