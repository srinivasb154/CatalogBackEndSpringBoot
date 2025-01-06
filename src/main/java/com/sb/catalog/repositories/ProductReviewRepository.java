package com.sb.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sb.catalog.models.ProductReview;
import com.sb.catalog.models.ProductReviewId;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, ProductReviewId> {

    // Find all reviews for a specific product
    List<ProductReview> findByProductId(UUID productId);

    // Find reviews by product ID and user name
    List<ProductReview> findByProductIdAndUserName(UUID productId, String userName);

    @Query("SELECT ps, p.productName FROM ProductReview ps JOIN Product p ON ps.productId = p.productId")
    List<Object[]> findAllWithProductName();
}
