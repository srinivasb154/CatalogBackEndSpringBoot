package com.sb.catalog.services;

import com.sb.catalog.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sb.catalog.models.ProductReview;
import com.sb.catalog.models.ProductReviewId;
import com.sb.catalog.repositories.ProductReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;

    public ProductReviewService(ProductReviewRepository productReviewRepository,
                                ProductRepository productRepository) {
        this.productReviewRepository = productReviewRepository;
        this.productRepository = productRepository;
    }

    // Get all reviews for a product
    public List<ProductReview> getReviewsByProductId(UUID productId) {
        return productReviewRepository.findByProductId(productId);
    }

    // Get reviews for a specific product and user
    public List<ProductReview> getReviewsByProductIdAndUserName(UUID productId, String userName) {
        return productReviewRepository.findByProductIdAndUserName(productId, userName);
    }

    // Save a new review
    @Transactional
    public ProductReview saveReview(ProductReview productReview) {
        UUID productId = productReview.getProductId();
        String userName = productReview.getUserName();

        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Invalid product ID: " + productId);
        }

        // Fetch all reviews for this product and user
        List<ProductReview> existingReviews = productReviewRepository.findByProductIdAndUserName(productId, userName);

        int nextCommentId;
        if (!existingReviews.isEmpty()) {
            // Get the highest commentId for this product and user
            nextCommentId = existingReviews.stream()
                    .mapToInt(ProductReview::getCommentId)
                    .max()
                    .orElse(0) + 1;
        } else {
            // Start with 1 if no reviews exist for this combination
            nextCommentId = 1;
        }

        // Set the new commentId
        productReview.setCommentId(nextCommentId);

        // Save the review
        return productReviewRepository.save(productReview);
    }

    // Delete a review by its composite key
    public void deleteReview(ProductReviewId productReviewId) {
        productReviewRepository.deleteById(productReviewId);
    }

    // Get a review by its composite key
    public Optional<ProductReview> getReviewById(ProductReviewId productReviewId) {
        return productReviewRepository.findById(productReviewId);
    }
}

