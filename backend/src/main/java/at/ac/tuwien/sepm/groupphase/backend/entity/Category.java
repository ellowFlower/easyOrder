package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 10000)
    private String description;

    @Column(nullable = false)
    private Boolean show;

    @Lob
    @Column(nullable = true, columnDefinition = "CLOB")
    private String image;

    @Column(nullable = true)
    private String imageContentType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "food_categories",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private Set<Food> assignedFood;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "drink_categories",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "drink_id")
    )
    private Set<Drink> assignedDrink;

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

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Food> getAssignedFood() {
        return assignedFood;
    }

    public void setAssignedFood(Set<Food> assignedFood) {
        this.assignedFood = assignedFood;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Drink> getAssignedDrink() {
        return assignedDrink;
    }

    public void setAssignedDrink(Set<Drink> assignedDrink) {
        this.assignedDrink = assignedDrink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
            Objects.equals(name, category.name) &&
            Objects.equals(description, category.description) &&
            Objects.equals(show, category.show) &&
            Objects.equals(image, category.image) &&
            Objects.equals(imageContentType, category.imageContentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, show, image, imageContentType);
    }

    @Override
    public String toString() {
        return "Food{" +
            "id=" + id +
            ", name=" + name +
            ", description='" + description + '\'' +
            ", isShown='" + show + '\'' +
            ", image='" + image + '\'' +
            ", imageContentType='" + imageContentType + '}';
    }

    public static final class CategoryBuilder {
        private Long id;
        private String name;
        private String description;
        private Boolean show;
        private String image;
        private String imageContentType;

        private CategoryBuilder() {
        }

        public static CategoryBuilder aCategory() {
            return new CategoryBuilder();
        }

        public CategoryBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CategoryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CategoryBuilder withIsShown(Boolean show) {
            this.show = show;
            return this;
        }

        public CategoryBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public CategoryBuilder withImageContentType(String imageContentType) {
            this.imageContentType = imageContentType;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.setId(id);
            category.setName(name);
            category.setDescription(description);
            category.setShow(show);
            category.setImage(image);
            category.setImageContentType(imageContentType);
            return category;
        }
    }
}
