package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CompleteFoodDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DrinkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Category;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
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
public class SpecificDrinkMapper {

    @Autowired
    private CategoryService categoryService;

    public Drink drinkDtoToDrink(DrinkDto drinkDto){
        Drink drink = new Drink();
        drink.setId(drinkDto.getId());
        drink.setName(drinkDto.getName());
        drink.setDescription(drinkDto.getDescription());
        drink.setPrice(drinkDto.getPrice());
        drink.setImage(drinkDto.getImage());
        drink.setImageContentType(drinkDto.getImageContentType());
        drink.setAlcohol(drinkDto.getAlcohol());
        drink.setDeleted(drinkDto.getDeleted());
        drink.setUpdated(drinkDto.getUpdated());

        List<Category> categories = new ArrayList<>();

        for (Long catId: drinkDto.getCategoryIds()){
            Category cat = categoryService.findOne(catId);
            categories.add(cat);
        }

        drink.setAssignedCategories(categories);
        return drink;
    }

    public DrinkDto drinkToDrinkDto(Drink drink){
        DrinkDto drinkDto = new DrinkDto();
        drinkDto.setId(drink.getId());
        drinkDto.setName(drink.getName());
        drinkDto.setDescription(drink.getDescription());
        drinkDto.setPrice(drink.getPrice());
        drinkDto.setImage(drink.getImage());
        drinkDto.setImageContentType(drink.getImageContentType());
        drinkDto.setAlcohol(drink.getAlcohol());
        drinkDto.setDeleted(drink.isDeleted());
        drinkDto.setUpdated(drink.isUpdated());

        List<Long> catIds= new ArrayList<>();

        for (Category cat: drink.getAssignedCategories()){
            catIds.add(cat.getId());
        }

        drinkDto.setCategoryIds(catIds);
        return drinkDto;
    }

    @IterableMapping(qualifiedByName = "drink")
    public List<DrinkDto> drinkToDrinkDto(List<Drink> drink){
        List<DrinkDto> dtoList = new ArrayList<>();
        for (Drink f: drink){
            dtoList.add(drinkToDrinkDto(f));
        }

        return dtoList;
    }
}
