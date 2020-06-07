package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Drink;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemStatisticDto {
    String name;
    BigDecimal value = new BigDecimal(0);

    public ItemStatisticDto(Food food){
        this.name = food.getName();
    }

    public ItemStatisticDto(Drink drink){
        this.name = drink.getName();
    }

    public ItemStatisticDto(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemStatisticDto)) return false;
        ItemStatisticDto that = (ItemStatisticDto) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() { return Objects.hash(name, value); }

    @Override
    public String toString() {
        return "{" +
            "\"name\": " + "\"" + name + "\"" +
            ", \"value\": " + value +
            "}";
    }
}
