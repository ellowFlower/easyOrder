package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SimpleOrderDto {

    private Long id;

    private Status status;

    private String assistance;

    private Date startDate;

    private Date endDate;

    private List<SimpleFoodDto> foods = new ArrayList<>();

    private List<SimpleDrinkDto> drinks = new ArrayList<>();

    private Long tableId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleOrderDto)) return false;
        SimpleOrderDto that = (SimpleOrderDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(assistance, that.assistance) &&
            Objects.equals(startDate, that.startDate)&&
            Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, assistance);
    }

    @Override
    public String toString() {
        return "{" +
            "\"id\": " + id +
            ", \"status\": " + "\"" + status + "\"" +
            ", \"assistance\": " + "\"" + assistance + "\"" +
            ", \"startDate\": " + startDate +
            ", \"endDate\": " + endDate +
            ", \"tableId\": " + tableId +
            "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssistance() {
        return assistance;
    }

    public void setAssistance(String assistance) {
        this.assistance = assistance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<SimpleFoodDto> getFoods() {
        return foods;
    }

    public void setFoods(List<SimpleFoodDto> foods) {
        this.foods = foods;
    }

    public List<SimpleDrinkDto> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<SimpleDrinkDto> drinks) {
        this.drinks = drinks;
    }

    public static final class SimpleOrderDtoBuilder {

        private Long id;
        private Status status;
        private String assistance;
        private Date startDate;
        private Date endDate;
        private Long tableId;


        private SimpleOrderDtoBuilder() {
        }

        public static SimpleOrderDto.SimpleOrderDtoBuilder aOrder() {
            return new SimpleOrderDto.SimpleOrderDtoBuilder();
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withAssistance(String assistance) {
            this.assistance = assistance;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withTable(Long tableId) {
            this.tableId = tableId;
            return this;
        }


        public SimpleOrderDto build() {
            SimpleOrderDto order = new SimpleOrderDto();
            order.setId(id);
            order.setStatus(status);
            order.setAssistance(assistance);
            order.setStartDate(startDate);
            order.setEndDate(endDate);
            order.setTableId(tableId);

            return order;
        }



    }
}
