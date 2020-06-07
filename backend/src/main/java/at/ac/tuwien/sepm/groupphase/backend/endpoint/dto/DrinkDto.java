package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Food;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class DrinkDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer alcohol;
    private String image;
    private String imageContentType;
    private List<Long> categoryIds;
    private Boolean deleted;
    private Boolean updated;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Integer getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrinkDto)) return false;
        DrinkDto that = (DrinkDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(alcohol, that.alcohol) &&
            Objects.equals(image, that.image) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(updated, that.updated) &&
            Objects.equals(imageContentType, that.imageContentType);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, description, price, alcohol, image, imageContentType, deleted, updated); }

    @Override
    public String toString() {
        return "{" +
            "\"id\": " + id +
            ", \"name\": " + "\"" + name + "\"" +
            ", \"description\": " + "\"" + description + "\"" +
            ", \"price\": " + price +
            ", \"alcohol\": " + alcohol +
            ", \"image\": " + "\"" + image + "\"" +
            ", \"imageContentType\": " + "\"" + imageContentType + "\"" +
            ", \"categoryIds\": " + categoryIds +
            ", \"deleted\": " + "\"" + deleted + "\"" +
            ", \"updated\": " + "\"" + updated + "\"" +
            "}";
    }

    public static final class DrinkDtoBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String image;
        private String imageContentType;
        private Integer alcohol;
        private Boolean deleted;
        private Boolean updated;
        private List<Long> categoryIds;

        private DrinkDtoBuilder() {         }

        public static DrinkDtoBuilder aDrinkDto() {
            return new DrinkDtoBuilder();
        }

        public DrinkDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public DrinkDtoBuilder withDeleted(Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public DrinkDtoBuilder withUpdated(Boolean updated) {
            this.updated = updated;
            return this;
        }

        public DrinkDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DrinkDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public DrinkDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public DrinkDtoBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public DrinkDtoBuilder withImageContentType(String imageContentType) {
            this.imageContentType = imageContentType;
            return this;
        }

        public DrinkDtoBuilder withAlcohol(Integer alcohol) {
            this.alcohol = alcohol;
            return this;
        }

        public DrinkDtoBuilder withCategoryIds(List<Long> categoryIds) {
            this.categoryIds = categoryIds;
            return this;
        }

        public DrinkDto build() {
            DrinkDto drink = new DrinkDto();
            drink.setId(id);
            drink.setName(name);
            drink.setDescription(description);
            drink.setPrice(price);
            drink.setAlcohol(alcohol);
            drink.setImage(image);
            drink.setImageContentType(imageContentType);
            drink.setCategoryIds(categoryIds);
            drink.setDeleted(deleted);
            drink.setUpdated(updated);
            return drink;
        }
    }
}

