package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class ImagelessCategoryDto {

    private Long id;
    private String name;
    private String description;
    private Boolean show;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImagelessCategoryDto)) return false;
        ImagelessCategoryDto that = (ImagelessCategoryDto) o;
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
            "}";
    }

    public static final class ImagelessCategoryDtoBuilder {
        private Long id;
        private String name;
        private String description;
        private Boolean show;

        private ImagelessCategoryDtoBuilder() {
        }

        public static ImagelessCategoryDtoBuilder aCompleteImagelessCategoryDto() {
            return new ImagelessCategoryDtoBuilder();
        }

        public ImagelessCategoryDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ImagelessCategoryDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ImagelessCategoryDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ImagelessCategoryDtoBuilder withShow(Boolean show) {
            this.show = show;
            return this;
        }

        public ImagelessCategoryDto build() {
            ImagelessCategoryDto category = new ImagelessCategoryDto();
            category.setId(id);
            category.setName(name);
            category.setDescription(description);
            category.setShow(show);
            return category;
        }
    }
}
