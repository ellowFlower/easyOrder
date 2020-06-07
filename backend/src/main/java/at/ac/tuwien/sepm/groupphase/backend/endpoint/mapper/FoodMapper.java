package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CompleteFoodDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.exception.MultipartFileToByteArrayException;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Mapper
public interface FoodMapper {

    Food completeFoodDtoToFood(CompleteFoodDto completeFoodDto);

    CompleteFoodDto foodToCompleteFoodDto(Food food);

    @IterableMapping(qualifiedByName = "food")
    List<CompleteFoodDto> foodToCompleteFoodDto(List<Food> food);

}
