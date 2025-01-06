package com.sb.catalog.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id", nullable = false, unique = true)
    @JsonProperty("_id")
    private UUID categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "parent_category")
    private String parentCategory;

    @Column(name = "description")
    private String description;

    @Column(name = "sort_order")
    private String sortOrder;

    @Column(name = "is_visible", nullable = false)
    private boolean isVisible;

    @Column(name = "smart_category", nullable = false)
    private boolean smartCategory;

    @Column(name = "product_must_watch", nullable = false)
    private boolean productMustWatch;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    public Category(UUID uuid, String electronics) {
    }
}
