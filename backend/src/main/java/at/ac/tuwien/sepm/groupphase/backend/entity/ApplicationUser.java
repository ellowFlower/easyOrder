package at.ac.tuwien.sepm.groupphase.backend.entity;

//TODO: replace this class with a correct ApplicationUser Entity implementation

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class ApplicationUser {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = true, unique = false, length = 50)
    private String email;

    @Column(nullable = false, unique = false, length = 100)
    private String password;

    @Column(nullable = false, unique = false, length = 100)
    private String admintype;


    @OneToOne(mappedBy = "applicationUser", cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    private TableInfo tableInfo;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> Orders;


    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public ApplicationUser() {

    }

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

    public String getAdmintype() {
        return admintype;
    }

    public void setAdmintype(String admintype) {
        this.admintype = admintype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password,admintype);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", Name=" + name +
            ", email='" + email + '\'' +
            ", admintype='" + admintype + '\'' +
            '}';
    }

}
