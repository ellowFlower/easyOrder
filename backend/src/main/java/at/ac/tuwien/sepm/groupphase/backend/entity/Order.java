package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;

import javax.persistence.*;

import java.util.*;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String assistance;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private ApplicationUser table;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "food_orders",
        joinColumns = @JoinColumn(name = "orders_id"),
        inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "drink_orders",
        joinColumns = @JoinColumn(name = "orders_id"),
        inverseJoinColumns = @JoinColumn(name = "drink_id")
    )
    private List<Drink> drinks = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
            Objects.equals(status, order.status) &&
            Objects.equals(assistance, order.assistance) &&
            Objects.equals(startDate, order.startDate)&&
            Objects.equals(endDate, order.endDate);
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
            "}";
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
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

    public ApplicationUser getTable() {
        return table;
    }

    public void setTable(ApplicationUser table) {
        this.table = table;
    }


    public void removeFood(Food food){



    }


    public static final class OrderBuilder {

        private Long id;
        private Status status;
        private String assistance;
        private Date startDate;
        private Date endDate;
        private ApplicationUser table;
        private List<Food> foods;
        private List<Drink> drinks;


        private OrderBuilder() {
        }

        public static OrderBuilder aOrder() {
            return new OrderBuilder();
        }

        public OrderBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public OrderBuilder withAssistance(String assistance) {
            this.assistance = assistance;
            return this;
        }

        public OrderBuilder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public OrderBuilder withEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public OrderBuilder withTable(ApplicationUser table) {
            this.table = table;
            return this;
        }

        public OrderBuilder withFoods(List<Food> foods) {
            this.foods = foods;
            return this;
        }

        public OrderBuilder withDrinks(List<Drink> drinks) {
            this.drinks = drinks;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.setId(id);
            order.setStatus(status);
            order.setAssistance(assistance);
            order.setStartDate(startDate);
            order.setEndDate(endDate);
            order.setTable(table);
            order.setDrinks(drinks);
            order.setFoods(foods);

            return order;
        }



    }
}
