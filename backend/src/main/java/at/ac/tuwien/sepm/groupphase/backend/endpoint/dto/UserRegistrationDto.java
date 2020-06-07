package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserRegistrationDto {


    @NotNull(message = "Email must not be null")
    @Email
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

    @NotNull(message ="Name must not be null")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(o instanceof UserRegistrationDto)) return false;
        UserRegistrationDto userRegistrationDto = (UserRegistrationDto) o;
        return Objects.equals(email, userRegistrationDto.email) &&
            Objects.equals(password, userRegistrationDto.password)&&
            Objects.equals(name, userRegistrationDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password,name);
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            '}';
    }


    public static final class UserRegistrationDtoBuilder {
        private String email;
        private String password;
        private String name;

        private UserRegistrationDtoBuilder() {
        }

        public static UserRegistrationDtoBuilder anUserLoginDto() {
            return new UserRegistrationDtoBuilder();
        }

        public UserRegistrationDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserRegistrationDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserRegistrationDtoBuilder withname(String name) {
            this.name = name;
            return this;
        }

        public UserRegistrationDto build() {
            UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setEmail(email);
            userRegistrationDto.setPassword(password);
            userRegistrationDto.setName(name);
            return userRegistrationDto;
        }
    }



}
