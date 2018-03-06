package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private OrderDetailsService orderDetailsService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, @Lazy OrderDetailsService orderDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderDetailsService = orderDetailsService;
    }

    @Transactional
    @Override
    public User registerNewUserAccount(UserDTO accountDto)
            throws EmailExistsException {

        if (emailExist(accountDto.getUsername())) {
            throw new EmailExistsException("There is an account with that email address:"
                    + accountDto.getUsername());
        }
        User user = new User();
        user.setFirstName(accountDto.getFirst_name());
        user.setLastName(accountDto.getLast_name());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setUsername(accountDto.getUsername());
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByRole("BUYER"), roleRepository.findByRole("SELLER"))));
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public void addProductToOrders(Product product, String buyer) {
        User user = userRepository.findByUsername(buyer);

        OrderDetails orderDetails = orderDetailsService.createOrderDetails(product.getId(), user.getId());

        Set<Order> orders = new HashSet<>();
        orders.add(orderDetails.getOrder());

        user.setOrders(orders);

        userRepository.save(user);
    }

    private boolean emailExist(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return true;
        }

        return false;
    }
}
