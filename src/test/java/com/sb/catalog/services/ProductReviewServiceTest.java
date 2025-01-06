package com.sb.catalog.services;

import com.sb.catalog.models.ProductReview;
import com.sb.catalog.models.ProductReviewId;
import com.sb.catalog.repositories.ProductReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductReviewServiceTest {

    @Mock
    private ProductReviewRepository productReviewRepository;

    @InjectMocks
    private ProductReviewService productReviewService;

    private AutoCloseable closeable;

    private UUID productId;
    private String userName;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();
        userName = "testUser";
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testSaveReview_NewCombination_StartsWithOne() {
        // Arrange
        ProductReview review = ProductReview.builder()
                .productId(productId)
                .userName(userName)
                .comment("Great product!")
                .rating(5)
                .build();

        when(productReviewRepository.findByProductIdAndUserName(productId, userName)).thenReturn(List.of());
        when(productReviewRepository.save(any(ProductReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductReview savedReview = productReviewService.saveReview(review);

        // Assert
        assertNotNull(savedReview);
        assertEquals(1, savedReview.getCommentId());
        verify(productReviewRepository, times(1)).findByProductIdAndUserName(productId, userName);
        verify(productReviewRepository, times(1)).save(review);
    }

    @Test
    public void testSaveReview_ExistingCombination_IncrementsCommentId() {
        // Arrange
        ProductReview existingReview = ProductReview.builder()
                .productId(productId)
                .userName(userName)
                .commentId(1)
                .comment("Nice product!")
                .rating(4)
                .build();

        ProductReview review = ProductReview.builder()
                .productId(productId)
                .userName(userName)
                .comment("Great product!")
                .rating(5)
                .build();

        when(productReviewRepository.findByProductIdAndUserName(productId, userName)).thenReturn(List.of(existingReview));
        when(productReviewRepository.save(any(ProductReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductReview savedReview = productReviewService.saveReview(review);

        // Assert
        assertNotNull(savedReview);
        assertEquals(2, savedReview.getCommentId());
        verify(productReviewRepository, times(1)).findByProductIdAndUserName(productId, userName);
        verify(productReviewRepository, times(1)).save(review);
    }

    @Test
    public void testGetReviewsByProductId() {
        // Arrange
        ProductReview review = ProductReview.builder()
                .productId(productId)
                .userName(userName)
                .commentId(1)
                .comment("Good product!")
                .rating(4)
                .build();

        when(productReviewRepository.findByProductId(productId)).thenReturn(List.of(review));

        // Act
        List<ProductReview> reviews = productReviewService.getReviewsByProductId(productId);

        // Assert
        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(review, reviews.getFirst());
        verify(productReviewRepository, times(1)).findByProductId(productId);
    }

    @Test
    public void testDeleteReview() {
        // Arrange
        ProductReviewId reviewId = new ProductReviewId(productId, userName, 1);
        doNothing().when(productReviewRepository).deleteById(reviewId);

        // Act
        productReviewService.deleteReview(reviewId);

        // Assert
        verify(productReviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    public void testGetReviewById() {
        // Arrange
        ProductReviewId reviewId = new ProductReviewId(productId, userName, 1);
        ProductReview review = ProductReview.builder()
                .productId(productId)
                .userName(userName)
                .commentId(1)
                .comment("Excellent!")
                .rating(5)
                .build();

        when(productReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Act
        Optional<ProductReview> result = productReviewService.getReviewById(reviewId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(review, result.get());
        verify(productReviewRepository, times(1)).findById(reviewId);
    }
}
