package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Food;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CompleteFoodDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String image;

    private Boolean deleted;

    private Boolean updated;

    private String imageContentType;

    private List<Long> categoryIds;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

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

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompleteFoodDto)) return false;
        CompleteFoodDto that = (CompleteFoodDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(updated, that.updated) &&
            Objects.equals(categoryIds, that.categoryIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, image, imageContentType, categoryIds, deleted, updated);
    }

    @Override
    public String toString() {
        return "{" +
            "\"id\": " + id +
            ", \"name\": " + "\"" + name + "\"" +
            ", \"description\": " + "\"" + description + "\"" +
            ", \"price\": " + price +
            ", \"image\": " + "\"" + image + "\"" +
            ", \"imageContentType\": " + "\"" + imageContentType + "\"" +
            ", \"categoryIds\": " + categoryIds +
            ", \"deleted\": " + "\"" + deleted + "\"" +
            ", \"updated\": " + "\"" + updated + "\"" +
            "}";
    }

    public static final class CompleteFoodDtoBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String image;
        private String imageContentType;
        private Boolean deleted;
        private Boolean updated;
        private List<Long> categoryIds;

        private CompleteFoodDtoBuilder() {
        }

        public static CompleteFoodDtoBuilder aCompleteFoodDto() {
            return new CompleteFoodDtoBuilder();
        }

        public CompleteFoodDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CompleteFoodDtoBuilder withDeleted(Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public CompleteFoodDtoBuilder withUpdated(Boolean updated) {
            this.updated = updated;
            return this;
        }

        public CompleteFoodDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CompleteFoodDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CompleteFoodDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public CompleteFoodDtoBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public CompleteFoodDtoBuilder withImageContentType (String imageContentType){
            this.imageContentType = imageContentType;
            return this;
        }

        public CompleteFoodDtoBuilder withCategoryIds (List<Long> categoryIds){
            this.categoryIds = categoryIds;
            return this;
        }

        public CompleteFoodDto build() {
            CompleteFoodDto food = new CompleteFoodDto();
            food.setId(id);
            food.setName(name);
            food.setDescription(description);
            food.setPrice(price);
            food.setImage(image);
            food.setImageContentType(imageContentType);
            food.setCategoryIds(categoryIds);
            food.setDeleted(deleted);
            food.setUpdated(updated);
            return food;
        }
    }
}
