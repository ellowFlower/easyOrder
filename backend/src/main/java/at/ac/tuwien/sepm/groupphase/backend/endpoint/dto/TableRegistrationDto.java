package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TableRegistrationDto {



    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

    @NotNull(message ="Name must not be null")
    private String name;

    @NotNull(message ="Seats must not be null")
    private int seats;


    public TableRegistrationDto() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableRegistrationDto)) return false;
        TableRegistrationDto userRegistrationDto = (TableRegistrationDto) o;
        return Objects.equals(email, userRegistrationDto.email) &&
            Objects.equals(password, userRegistrationDto.password)&&
            Objects.equals(name, userRegistrationDto.name)&&
            Objects.equals(seats, userRegistrationDto.seats);
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
            ", seats='" + seats + '\'' +
            '}';
    }


    public static final class UserRegistrationDtoBuilder {
        private String email;
        private String password;
        private String name;
        private int seats;

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

        public TableRegistrationDto build() {
            TableRegistrationDto tableRegistrationDto = new TableRegistrationDto();
            tableRegistrationDto.setEmail(email);
            tableRegistrationDto.setPassword(password);
            tableRegistrationDto.setName(name);
            tableRegistrationDto.setSeats(seats);
            return tableRegistrationDto;
        }
    }



}
