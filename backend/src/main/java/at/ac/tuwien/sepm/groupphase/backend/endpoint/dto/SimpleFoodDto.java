package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

public class SimpleFoodDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleFoodDto)) return false;
        SimpleFoodDto that = (SimpleFoodDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price);
    }

    @Override
    public String toString() {
        return "{" +
            "\"id\": " + id +
            ", \"name\": " + "\"" + name + "\"" +
            ", \"description\": " + "\"" + description + "\"" +
            ", \"price\": " + price +
            "}";
    }

    public static final class SimpleFoodDtoBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String image;
        private String imageContentType;

        private SimpleFoodDtoBuilder() {
        }

        public static SimpleFoodDtoBuilder aSimpleFoodDto() {
            return new SimpleFoodDtoBuilder();
        }

        public SimpleFoodDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SimpleFoodDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SimpleFoodDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public SimpleFoodDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public SimpleFoodDtoBuilder withImage(String image) {
            this.image = image;
            return this;
        }

        public SimpleFoodDtoBuilder withImageContentType (String imageContentType){
            this.imageContentType = imageContentType;
            return this;
        }

        public SimpleFoodDto build() {
            SimpleFoodDto food = new SimpleFoodDto();
            food.setId(id);
            food.setName(name);
            food.setDescription(description);
            food.setPrice(price);
            return food;
        }
    }
}
