package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.AlreadyRegisteredExeption;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TableInfoRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final TableInfoRepository tableInfoRepository;
    private final Validator validator;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder, TableInfoRepository tableInfoRepository, Validator validator, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tableInfoRepository = tableInfoRepository;
        this.validator = validator;
        this.orderRepository = orderRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getAdmintype().equals("Admin"))
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            else
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");

            return new User(applicationUser.getName(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String name) {
        LOGGER.debug("Find application user by name");
        ApplicationUser applicationUser = userRepository.findByName(name);
        if (applicationUser != null) return applicationUser;
        throw new NotFoundException(String.format("Could not find the user with the name %s", name));
    }

    @Override
    public ApplicationUser createApplicationUser(ApplicationUser user) throws AlreadyRegisteredExeption{

        if(userRepository.findByName(user.getName())!= null){
            throw new AlreadyRegisteredExeption("Already Registered this Name!");
        }else {

            String encodedpassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedpassword);
            return userRepository.save(user);

        }
    }

    @Override
    public ApplicationUser findOne(Long id) {
        LOGGER.debug("Get application user with id {}", id);
        validator.validateId(id);

        Optional<ApplicationUser> applicationUser = userRepository.findById(id);
        validator.checkIfFoundEntry(applicationUser);

        return applicationUser.get();
    }

    @Override
    public List<ApplicationUser> findAll() {
        LOGGER.debug("Get all application users");

        return userRepository.findByAdmintype("Table");
    }

    @Override
    public void deleteTable(String name) {
        LOGGER.debug("Delete table with name {}", name);

        List<String> usedTables = orderRepository.getTableNames();

        // check if table is in use
        validator.validateTableUse(usedTables, name);

        // loop over the orders with status ERLEDIGT and delete the reference

        // get all orders with specific table and status ERLEDIGT
        List<Order> orders = orderRepository.getOrdersWithTableId(userRepository.findByName(name).getId());

        for (Order o : orders) {
            o.setTable(null);
        }

        userRepository.deleteByName(name);
    }


}
