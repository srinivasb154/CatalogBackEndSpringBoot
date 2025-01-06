package com.sb.catalog.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ProductReviewId implements Serializable {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "comment_id")
    private Integer commentId;

    public ProductReviewId() {}

    public ProductReviewId(UUID productId, String userName, Integer commentId) {
        this.productId = productId;
        this.userName = userName;
        this.commentId = commentId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReviewId that = (ProductReviewId) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(commentId, that.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userName, commentId);
    }
}
