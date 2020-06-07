package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class CategoryDto {

    private Long id;

    private String name;

    private String description;

    private Boolean show;

    private String image;

    private String imageContentType;

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

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryDto)) return false;
        CategoryDto that = (CategoryDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(show, that.show);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, show);
    }

    @Override
    public String toString() {
        return "{" +
            "\"id\": " + id +
            ", \"name\": " + "\"" + name + "\"" +
            ", \"description\": " + "\"" + description + "\"" +
            ", \"show\": " + "\"" + show + "\"" +
            ", \"image\": " + "\"" + image + "\"" +
            ", \"imageContentType\": " + "\"" + imageContentType + "\"" +
            "}";
    }

    public static final class CategoryDtoBuilder {
        private Long id;
        private String name;
        private String description;
        private Boolean show;
        private String image;
        private String imageContentType;

        private CategoryDtoBuilder() {
        }

        public static CategoryDtoBuilder aCompleteCategoryDto() {
            return new CategoryDtoBuilder();
        }

        public CategoryDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CategoryDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CategoryDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CategoryDtoBuilder withShow(Boolean show) {
            this.show = show;
            return this;
        }

        public CategoryDtoBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public CategoryDtoBuilder withImageContentType (String imageContentType){
            this.imageContentType = imageContentType;
            return this;
        }

        public CategoryDto build() {
            CategoryDto category = new CategoryDto();
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
