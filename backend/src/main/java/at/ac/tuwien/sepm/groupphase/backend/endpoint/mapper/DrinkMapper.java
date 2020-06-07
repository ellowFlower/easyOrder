package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DrinkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper
public interface DrinkMapper {
    Drink drinkDtoToDrink(DrinkDto drinkDto);

    DrinkDto drinkToDrinkDto(Drink drink);

    @IterableMapping(qualifiedByName = "food")
    List<DrinkDto> drinkToDrinkDto(List<Drink> drink);

}
