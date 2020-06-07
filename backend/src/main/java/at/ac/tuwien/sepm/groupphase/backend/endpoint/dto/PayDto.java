package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PayDto {
    private Long tableId;

    private List<String> foods = new ArrayList<>();

    private List<String> drinks = new ArrayList<>();

    private BigDecimal price = new BigDecimal("0");

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<String> getFoods() {
        return foods;
    }

    public void setFoods(List<String> foods) {
        this.foods = foods;
    }

    public List<String> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<String> drinks) {
        this.drinks = drinks;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void addFood(Food f) {
        this.foods.add(f.getName() + "  " + f.getPrice());
    }

    public void addDrink(Drink d) {
        this.drinks.add(d.getName() + "  " + d.getPrice());
    }

    public void addPrice(BigDecimal p) {
        this.price = this.price.add(p);
    }
}
