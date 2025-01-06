package com.sb.catalog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_asset")
public class ProductAsset {

    @Id
    @Column(name = "product_asset_id", updatable = false, nullable = false)
    private Integer productAssetId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "type")
    private String type;

    @Column(name = "extension")
    private String extension;

    @Column(name = "binary_data", columnDefinition="bytea")
    private byte[] binaryData;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
