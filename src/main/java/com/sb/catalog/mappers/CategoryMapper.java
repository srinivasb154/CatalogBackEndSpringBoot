package com.sb.catalog.mappers;

import com.sb.catalog.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    default Category fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setCategoryId(id);
        return category;
    }

    default UUID toId(Category category) {
        return category != null ? category.getCategoryId() : null;
    }
}
