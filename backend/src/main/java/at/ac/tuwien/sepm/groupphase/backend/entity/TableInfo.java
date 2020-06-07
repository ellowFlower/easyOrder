package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TableInfo {

    @Id
    private Long id;

    @Column(nullable = false, unique = false)
    private int seats;

    @Column(nullable = false, unique = false)
    private Boolean isUsed;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private ApplicationUser applicationUser;


    public TableInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seats,isUsed);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", seats=" + seats +
            ", isUsed='" + isUsed + '\'' +
            '}';
    }


}
