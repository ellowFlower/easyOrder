package at.ac.tuwien.sepm.groupphase.backend.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StatisticDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

@Component
public class Validator {

    @Autowired
    private OrderRepository orderRepository;

    public void validateNewFood(Food food) throws ValidationException {

        //check, if image is base64 encoded
        String image = food.getImage();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(image);
        } catch(IllegalArgumentException e) {
            throw new ValidationException("image is not base64 encoded!");
        }

        //check, if image has content-type image
        String imageType = food.getImageContentType();
        if (!imageType.contains("image/")){
            throw new ValidationException("image has wrong content-type!");
        }

        //check, if category-list is null
        if (food.getCategories() == null){
            throw new ValidationException("List of assigned categories is null!");
        }
    }

    public void validateFoodPatch(Food food) throws ValidationException {
        //check id
        Long id = food.getId();
        if (id == null || id < 1){
            throw new ValidationException("invalid id.");
        }

        //check, if image is base64 encoded
        String image = food.getImage();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(image);
        } catch(IllegalArgumentException e) {
            throw new ValidationException("image is not base64 encoded!");
        }

        //check, if image has content-type image
        String imageType = food.getImageContentType();
        if (!imageType.contains("image/")){
            throw new ValidationException("image has wrong content-type!");
        }

        //check, if category-list is null
        if (food.getCategories() == null){
            throw new ValidationException("List of assigned categories is null!");
        }
    }

    public void validateNewDrink(Drink drink) throws ValidationException {

        //check, if image is base64 encoded
        String image = drink.getImage();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(image);
        } catch(IllegalArgumentException e) {
            throw new ValidationException("image is not base64 encoded!");
        }

        //check, if image has content-type image
        String imageType = drink.getImageContentType();
        if (!imageType.contains("image/")){
            throw new ValidationException("image has wrong content-type!");
        }
    }

    public void validateDrinkPatch(Drink drink) throws ValidationException {
        //check id
        Long id = drink.getId();

        //check, if image is base64 encoded
        String image = drink.getImage();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(image);
        } catch(IllegalArgumentException e) {
            throw new ValidationException("image is not base64 encoded!");
        }

        //check, if image has content-type image
        String imageType = drink.getImageContentType();
        if (!imageType.contains("image/")){
            throw new ValidationException("image has wrong content-type!");
        }
    }

    public void validateNewCategory(Category category) throws ValidationException {

        //check, if image is base64 encoded
        String image = category.getImage();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(image);
        } catch(IllegalArgumentException e) {
            throw new ValidationException("image is not base64 encoded!");
        }

        //check, if image has content-type image
        String imageType = category.getImageContentType();
        if (!imageType.contains("image/")){
            throw new ValidationException("image has wrong content-type!");
        }
    }

    public void validateCategoryPatch(Category category) throws ValidationException {
        //check id
        Long id = category.getId();

        //check, if image is base64 encoded
        String image = category.getImage();
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(image);
        } catch(IllegalArgumentException e) {
            throw new ValidationException("image is not base64 encoded!");
        }

        //check, if image has content-type image
        String imageType = category.getImageContentType();
        if (!imageType.contains("image/")){
            throw new ValidationException("image has wrong content-type!");
        }
    }

    public void validateId(Long id) throws ValidationException {
        if (id == null || id < 1){
            throw new ValidationException("invalid id.");
        }
    }

    public void validateOrderStatus(String status) throws ValidationException {
        boolean found = false;
        for (Status s : Status.values()) {
            if (s.name().equals(status)) {
                found = true;
            }
        }

        if (!found){
            throw new ValidationException("invalid status.");
        }
    }

    public void checkIfFoodisEmpty(List<Food> foods) throws ValidationException {
        if(foods.isEmpty()) {
            throw new NotFoundException("No foods to delete");
        }
    }

    public void checkIfFoundEntry(Optional<ApplicationUser> o) {
        if (!o.isPresent()) {
            throw new NotFoundException("Could not find Entry in database");
        }
    }

    public void validateStatisticDates(Date from, Date to){
        //check, if "from" is before "to"
        if (from.after(to)){
            throw new ValidationException("The start-date appears to be after the end-date of the required statistic.");
        }
        //check, if "from" is in the past
        if (from.after(new Date())){
            throw new ValidationException("The start-date of the required statistic appears to be in the future.");
        }
    }

    public void validateTableUse(List<String> tableNames, String toDeleteTable){
        if (!tableNames.isEmpty()) {
            for (String table : tableNames) {
                if (table.equals(toDeleteTable)) {
                    throw new ValidationException("The table is still in use.");
                }
            }
        }
    }



}
