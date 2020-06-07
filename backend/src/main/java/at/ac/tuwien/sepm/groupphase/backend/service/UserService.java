package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Food;
import at.ac.tuwien.sepm.groupphase.backend.exception.AlreadyRegisteredExeption;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address
     * <p>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param name the username
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String name) throws UsernameNotFoundException;

    /**
     * Find a application user based on the email address
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * create an application user
     *
     * @param user the application user
     * @return a application user
     */
    ApplicationUser createApplicationUser(ApplicationUser user) throws AlreadyRegisteredExeption;

    /**
     * get a application user by its ID
     *
     * @param id to be returned
     * @return application user with specified id
     */
    ApplicationUser findOne(Long id);

    /**
     * get all application user
     *
     * @return list of application users
     */
    List<ApplicationUser> findAll();

    /**
     * delete a specific table
     *
     * @param name of the table to be deleted
     */
    void deleteTable(String name);

}
