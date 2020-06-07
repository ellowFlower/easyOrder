package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class SimpleDrinkDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer alcohol;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleDrinkDto)) return false;
        SimpleDrinkDto that = (SimpleDrinkDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(alcohol, that.alcohol);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, description, price, alcohol); }

    @Override
    public String toString() {
        return "{" +
            "\"id\": " + id +
            ", \"name\": " + "\"" + name + "\"" +
            ", \"description\": " + "\"" + description + "\"" +
            ", \"price\": " + price +
            ", \"alcohol\": " + alcohol +
            "}";
    }

    public static final class SimpleDrinkDtoBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String image;
        private String imageContentType;
        private Integer alcohol;

        private SimpleDrinkDtoBuilder() {         }

        public static SimpleDrinkDtoBuilder aSimpleDrinkDto() {
            return new SimpleDrinkDtoBuilder();
        }

        public SimpleDrinkDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SimpleDrinkDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SimpleDrinkDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public SimpleDrinkDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public SimpleDrinkDtoBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public SimpleDrinkDtoBuilder withImageContentType(String imageContentType) {
            this.imageContentType = imageContentType;
            return this;
        }

        public SimpleDrinkDtoBuilder withAlcohol(Integer alcohol) {
            this.alcohol = alcohol;
            return this;
        }

        public SimpleDrinkDto build() {
            SimpleDrinkDto drink = new SimpleDrinkDto();
            drink.setId(id);
            drink.setName(name);
            drink.setDescription(description);
            drink.setPrice(price);
            drink.setAlcohol(alcohol);
            return drink;
        }
    }
}

