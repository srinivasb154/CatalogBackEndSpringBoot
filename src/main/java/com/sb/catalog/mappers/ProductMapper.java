package com.sb.catalog.mappers;

import com.sb.catalog.dto.*;
import com.sb.catalog.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, BrandMapper.class})
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "shortDescription", source = "shortDescription")
    @Mapping(target = "longDescription", source = "longDescription")
    @Mapping(target = "shippingNotes", source = "shippingNotes")
    @Mapping(target = "warrantyInfo", source = "warrantyInfo")
    @Mapping(target = "visibleToFrontEnd", source = "visibleToFrontEnd")
    @Mapping(target = "featuredProduct", source = "featuredProduct")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "brandId", source = "brandId")
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "productSpecifications.productSpec", ignore = true) // Avoid recursive mapping
    @Mapping(target = "productSpecifications", source = "specifications")
    @Mapping(target = "productAssets", source = "assets")
    Product toEntity(ProductRequest productRequest);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "productName", source = "productName")
    @Mapping(target = "sku", source = "sku")
    @Mapping(target = "shortDescription", source = "shortDescription")
    @Mapping(target = "longDescription", source = "longDescription")
    @Mapping(target = "shippingNotes", source = "shippingNotes")
    @Mapping(target = "warrantyInfo", source = "warrantyInfo")
    @Mapping(target = "visibleToFrontEnd", source = "visibleToFrontEnd")
    @Mapping(target = "featuredProduct", source = "featuredProduct")
    @Mapping(source = "categoryId", target = "categoryId")
    @Mapping(source = "brandId", target = "brandId")
    @Mapping(target = "specifications", source = "productSpecifications")
    @Mapping(target = "assets", source = "productAssets")
    ProductRequest toDto(Product product);

    @Mapping(target = "productSpec", ignore = true) // Avoid recursive mapping
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    ProductSpecifications toEntity(ProductSpecificationsDTO productSpecificationsDTO);

    ProductSpecificationsDTO toDto(ProductSpecifications productSpecifications);

    @Mapping(target = "product.productId", source = "productId")
    ProductAsset toEntity(ProductAssetsDTO productAssetsDTO);

    @Mapping(target = "productId", source = "product.productId")
    ProductAssetsDTO toDto(ProductAsset productAsset);

    ProductReview toEntity(ProductReviewDTO productReviewsDTO);

    ProductReviewDTO toDto(ProductReview productReview);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "bin", source = "bin")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "source", source = "source")
    @Mapping(target = "onHand", source = "onHand")
    @Mapping(target = "onHold", source = "onHold")
    ProductInventory toEntity(ProductInventoryDTO productInventoryDTO);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "bin", source = "bin")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "source", source = "source")
    @Mapping(target = "onHand", source = "onHand")
    @Mapping(target = "onHold", source = "onHold")
    ProductInventoryDTO toDto(ProductInventory productInventory);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "msrp", source = "msrp")
    @Mapping(target = "map", source = "map")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "sell", source = "sell")
    @Mapping(target = "base", source = "base")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "createdBy", source = "createdBy")
    ProductPricing toEntity(ProductPricingDTO productPricingDTO);

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "msrp", source = "msrp")
    @Mapping(target = "map", source = "map")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "sell", source = "sell")
    @Mapping(target = "base", source = "base")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "createdBy", source = "createdBy")
    ProductPricingDTO toDto(ProductPricing productPricing);
}
