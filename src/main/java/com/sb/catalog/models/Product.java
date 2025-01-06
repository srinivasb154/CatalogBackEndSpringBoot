package com.sb.catalog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", updatable = false, nullable = false)
    @JsonProperty("_id")
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "shipping_notes")
    private String shippingNotes;

    @Column(name = "warranty_info")
    private String warrantyInfo;

    @Column(name = "visible_to_front_end")
    private boolean visibleToFrontEnd;

    @Column(name = "featured_product")
    private boolean featuredProduct;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @JsonProperty("category")
    private Category categoryId;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    @JsonProperty("brand")
    private Brand brandId;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @OneToOne(mappedBy = "productSpec", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private ProductSpecifications productSpecifications;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductAsset> productAssets;
}
