package com.sb.catalog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_specifications")
public class ProductSpecifications {

    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
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

    @CreationTimestamp
    @Column(name = "create_at", updatable = false, nullable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product productSpec;
}
