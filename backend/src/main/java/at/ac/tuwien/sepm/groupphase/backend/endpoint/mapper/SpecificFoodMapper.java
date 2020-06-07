package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CompleteFoodDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CategoryService;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper
public class SpecificFoodMapper {

    @Autowired
    private CategoryService categoryService;

    public Food completeFoodDtoToFood(CompleteFoodDto completeFoodDto){
        Food food = new Food();
        food.setId(completeFoodDto.getId());
        food.setName(completeFoodDto.getName());
        food.setDescription(completeFoodDto.getDescription());
        food.setPrice(completeFoodDto.getPrice());
        food.setImage(completeFoodDto.getImage());
        food.setImageContentType(completeFoodDto.getImageContentType());
        food.setDeleted(completeFoodDto.getDeleted());
        food.setUpdated(completeFoodDto.getUpdated());

        List<Category> categories = new ArrayList<>();

        for (Long catId: completeFoodDto.getCategoryIds()){
            Category cat = categoryService.findOne(catId);
            categories.add(cat);
        }

        food.setCategories(categories);
        return food;
    }

    public CompleteFoodDto foodToCompleteFoodDto(Food food){
        CompleteFoodDto foodDto = new CompleteFoodDto();
        foodDto.setId(food.getId());
        foodDto.setName(food.getName());
        foodDto.setDescription(food.getDescription());
        foodDto.setPrice(food.getPrice());
        foodDto.setImage(food.getImage());
        foodDto.setImageContentType(food.getImageContentType());
        foodDto.setDeleted(food.isDeleted());
        foodDto.setUpdated(food.isUpdated());

        List<Long> catIds= new ArrayList<>();

        for (Category cat: food.getCategories()){
            catIds.add(cat.getId());
        }

        foodDto.setCategoryIds(catIds);
        return foodDto;
    }

    @IterableMapping(qualifiedByName = "food")
    public List<CompleteFoodDto> foodToCompleteFoodDto(List<Food> food){
        List<CompleteFoodDto> dtoList = new ArrayList<>();
        for (Food f: food){
            dtoList.add(foodToCompleteFoodDto(f));
        }

        return dtoList;
    }

}
