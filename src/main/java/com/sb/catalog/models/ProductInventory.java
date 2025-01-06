package com.sb.catalog.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventory {

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "bin", nullable = false)
    private String bin;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "on_hand", nullable = false)
    private Integer onHand;

    @Column(name = "on_hold", nullable = false)
    private Integer onHold;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updatedAt;
}
