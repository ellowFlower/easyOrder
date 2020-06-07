package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.enumeration.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    /**
     * Find ApplicationUser-entry fpr given email .
     * @email email of the ApplicationUser.
     * @return ApplicationUser entry for given email.
     */
    ApplicationUser findByEmail(String email);


    ApplicationUser findByName(String name);

    List<ApplicationUser> findByAdmintype(String table);

    void deleteByName(String name);
}
