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
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="brand_id")
    @JsonProperty("_id")
    private UUID brandId;

    @Column(name="brand_name", nullable = false)
    private String brandName;

    @Column
    private String description;

    @Column
    private String assets;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updateAt;
}
