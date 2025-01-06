package com.sb.catalog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewDTO {

    @JsonProperty("_id")
    private UUID productId;
    @JsonProperty("user")
    private String userName;

    @JsonIgnore
    private Integer commentId;

    private String comment;
    private Integer rating;
    private LocalDateTime createdAt;
}

