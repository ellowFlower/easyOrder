package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(message = "ID has to be an Integer starting at 1", value = 1L)
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

    @NotNull(message = "alcohol can't be empty")
    @Min(message = "alcohol has to be a positive Integer", value = 0)
    private Integer alcohol;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "assignedDrink")
    private List<Category> assignedCategories;

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

    public List<Category> getAssignedCategories() {
        return assignedCategories;
    }

    public void setAssignedCategories(List<Category> assignedCategories) {
        this.assignedCategories = assignedCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drink)) return false;
        Drink drink = (Drink) o;
        return Objects.equals(id, drink.id) &&
            Objects.equals(name, drink.name) &&
            Objects.equals(description, drink.description) &&
            Objects.equals(price, drink.price) &&
            Objects.equals(image, drink.image) &&
            Objects.equals(deleted, drink.deleted) &&
            Objects.equals(updated, drink.updated) &&
            Objects.equals(imageContentType, drink.imageContentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, image, imageContentType, alcohol, assignedCategories, deleted, updated);
    }

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
            ", \"deleted\": " + "\"" + deleted + "\"" +
            ", \"updated\": " + "\"" + updated + "\"" +
            "}";
    }

    public static final class DrinkBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String image;
        private String imageContentType;
        private Integer alcohol;
        private Boolean deleted;
        private Boolean updated;
        private List<Category> assignedCategories;

        private DrinkBuilder() {
        }

        public static DrinkBuilder aDrink() {
            return new DrinkBuilder();
        }

        public DrinkBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public DrinkBuilder withDeleted(Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public DrinkBuilder withUpdated(Boolean updated) {
            this.updated = updated;
            return this;
        }

        public DrinkBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DrinkBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public DrinkBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public DrinkBuilder withAlcohol(Integer alcohol) {
            this.alcohol = alcohol;
            return this;
        }

        public DrinkBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public DrinkBuilder withImageContentType(String imageContentType) {
            this.imageContentType = imageContentType;
            return this;
        }

        public DrinkBuilder withAssignedCategories(List<Category> assignedCategories) {
            this.assignedCategories = assignedCategories;
            return this;
        }

        public Drink build() {
            Drink drink = new Drink();
            drink.setId(id);
            drink.setName(name);
            drink.setDescription(description);
            drink.setPrice(price);
            drink.setAlcohol(alcohol);
            drink.setImage(image);
            drink.setImageContentType(imageContentType);
            drink.setAssignedCategories(assignedCategories);
            drink.setDeleted(deleted);
            drink.setUpdated(updated);
            return drink;
        }
    }

}
