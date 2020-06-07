package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Min(message = "ID has to be an Integer starting at 1", value = 1L)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name can't be empty or only a whitespace")
    private String name;

    @Column(nullable = false, length = 10000)
    @NotNull(message = "Description can't be empty")
    private String description;

    @Column(nullable = false)
    @DecimalMin(message = "Price has to be a positive number", value = "0.0")
    @NotNull(message = "Price can't be empty")
    private BigDecimal price;

    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    @NotBlank(message = "image can't be empty or only a whitespace")
    private String image;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "imageContentType can't be empty or only a whitespace")
    private String imageContentType;


    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "assignedFood")
    private List<Category> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Order> assignedOrder;

    @Column
    private Boolean deleted;

    @Column
    private Boolean updated;

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isUpdated() {
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return Objects.equals(id, food.id) &&
            Objects.equals(name, food.name) &&
            Objects.equals(description, food.description) &&
            Objects.equals(price, food.price) &&
            Objects.equals(image, food.image) &&
            Objects.equals(deleted, food.deleted) &&
            Objects.equals(updated, food.updated) &&
            Objects.equals(imageContentType, food.imageContentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, deleted, updated);
    }

    @Override
    public String toString() {
        return "{" +
            "\"id\": " + id +
            ", \"name\": " + "\"" + name + "\"" +
            ", \"description\": " + "\"" + description + "\"" +
            ", \"price\": " + price +
            ", \"image\": " + image +
            ", \"imageContentType\": " + imageContentType +
            ", \"deleted\": " + "\"" + deleted + "\"" +
            ", \"updated\": " + "\"" + updated + "\"" +
            "}";
    }

    public static final class FoodBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String image;
        private Boolean deleted;
        private Boolean updated;
        private String imageContentType;
        private List<Category> categories;

        private FoodBuilder() {
        }

        public static FoodBuilder aFood() {
            return new FoodBuilder();
        }

        public FoodBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public FoodBuilder withDeleted(Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public FoodBuilder withUpdated(Boolean updated) {
            this.updated = updated;
            return this;
        }

        public FoodBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public FoodBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public FoodBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public FoodBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public FoodBuilder withImageContentType(String imageContentType){
            this.imageContentType = imageContentType;
            return this;
        }

        public FoodBuilder withCategories(List<Category> categories){
            this.categories = categories;
            return this;
        }

        public Food build() {
            Food food = new Food();
            food.setId(id);
            food.setName(name);
            food.setDescription(description);
            food.setPrice(price);
            food.setImage(image);
            food.setImageContentType(imageContentType);
            food.setCategories(categories);
            food.setDeleted(deleted);
            food.setUpdated(updated);
            return food;
        }
    }
}
