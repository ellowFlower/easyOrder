package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImagelessCategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    //CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto categoryToCategoryDto(Category category);

    @IterableMapping(qualifiedByName = "category")
    List<CategoryDto> categoryToCategoryDto(List<Category> categories);

    Category categoryDtoToCategoryEntity(CategoryDto categoryDto);



    ImagelessCategoryDto categoryToImagelessCategoryDto(Category category);

    Category imagelessCategoryDtoToCategoryEntity(ImagelessCategoryDto imagelessCategoryDto);

    @IterableMapping(qualifiedByName = "category")
    List<ImagelessCategoryDto> categoryToImagelessCategoryDto(List<Category> categories);
}
