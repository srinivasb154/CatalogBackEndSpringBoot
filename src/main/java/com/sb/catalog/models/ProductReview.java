package com.sb.catalog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_reviews")
@IdClass(ProductReviewId.class)
public class ProductReview {

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Id
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Id
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating")
    private Integer rating;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
