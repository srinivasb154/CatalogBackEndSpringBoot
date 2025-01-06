package com.sb.catalog.repositories;

import com.sb.catalog.models.ProductAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductAssetRepository extends JpaRepository<ProductAsset, Integer> {

    // Find all assets associated with a product
    List<ProductAsset> findByProduct_ProductId(UUID productId);

    // Find a specific asset by productId and productAssetId
    Optional<ProductAsset> findByProduct_ProductIdAndProductAssetId(UUID productId, Integer productAssetId);

    @Query("SELECT ps, ps.product.productName FROM ProductAsset ps")
    List<Object[]> findAllWithProductName();
}
