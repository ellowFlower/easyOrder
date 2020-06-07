package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderDto {
    private Long id;

    private String status;

    private String assistance;

    private Date startDate;

    private Date endDate;

    private List<Integer> foodsId = new ArrayList<>();

    private List<Integer> drinksId = new ArrayList<>();

    private Long tableId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) &&
            status == orderDto.status &&
            Objects.equals(assistance, orderDto.assistance) &&
            Objects.equals(startDate, orderDto.startDate) &&
            Objects.equals(endDate, orderDto.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, assistance, startDate, endDate);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
            "id=" + id +
            ", status=" + status +
            ", assistance='" + assistance + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", tableId=" + tableId +
            '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public List<Integer> getFoodsId() {
        return foodsId;
    }

    public void setFoodsId(List<Integer> foodsId) {
        this.foodsId = foodsId;
    }

    public List<Integer> getDrinksId() {
        return drinksId;
    }

    public void setDrinksId(List<Integer> drinksId) {
        this.drinksId = drinksId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public static final class OrderDtoBuilder {

        private Long id;
        private String status;
        private String assistance;
        private Date startDate;
        private Date endDate;
        private Long tableId;


        private OrderDtoBuilder() {
        }

        public static OrderDto.OrderDtoBuilder aOrder() {
            return new OrderDtoBuilder();
        }

        public OrderDto.OrderDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public OrderDto.OrderDtoBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public OrderDto.OrderDtoBuilder withAssistance(String assistance) {
            this.assistance = assistance;
            return this;
        }

        public OrderDto.OrderDtoBuilder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public OrderDto.OrderDtoBuilder withEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public OrderDto.OrderDtoBuilder withTable(Long tableId) {
            this.tableId = tableId;
            return this;
        }


        public OrderDto build() {
            OrderDto order = new OrderDto();
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
