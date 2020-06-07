package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserLoginDto {

    @NotNull(message = "name must not be null")
    private String name;

    @NotNull(message = "Password must not be null")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLoginDto)) return false;
        UserLoginDto userLoginDto = (UserLoginDto) o;
        return Objects.equals(name, userLoginDto.name) &&
            Objects.equals(password, userLoginDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }

    @Override
    public String toString() {
        return "UserLoginDto{" +
            "name='" + name + '\'' +
            ", password='" + password + '\'' +
            '}';
    }


    public static final class UserLoginDtoBuilder {
        private String name;
        private String password;

        private UserLoginDtoBuilder() {
        }

        public static UserLoginDtoBuilder anUserLoginDto() {
            return new UserLoginDtoBuilder();
        }

        public UserLoginDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UserLoginDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserLoginDto build() {
            UserLoginDto userLoginDto = new UserLoginDto();
            userLoginDto.setName(name);
            userLoginDto.setPassword(password);
            return userLoginDto;
        }
    }
}
