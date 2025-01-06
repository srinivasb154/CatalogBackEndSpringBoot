package com.sb.catalog.mappers;

import com.sb.catalog.models.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    default Brand fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Brand brand = new Brand();
        brand.setBrandId(id);
        return brand;
    }

    default UUID toId(Brand brand) {
        return brand != null ? brand.getBrandId() : null;
    }
}

