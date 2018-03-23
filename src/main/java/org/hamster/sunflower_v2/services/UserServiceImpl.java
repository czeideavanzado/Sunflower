package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.SunflowerSmtpMailSender;
import org.hamster.sunflower_v2.domain.models.*;

import org.hamster.sunflower_v2.exceptions.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Service(value = "userService")
public class
UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private SunflowerSmtpMailSender sunflowerSmtpMailSender;
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, SunflowerSmtpMailSender sunflowerSmtpMailSender, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.sunflowerSmtpMailSender = sunflowerSmtpMailSender;
        this.verificationTokenRepository = verificationTokenRepository;
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
        user.setOrders(new HashSet<>());

        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
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
    public void addOrder(User user, Order order) {
        Set<Order> orders = user.getOrders();
        orders.add(order);

        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);

        String verificationLink = "<a href='https://localhost:8443/verifyAccount?token=" + token + "'>Verify my account</a>";

        try {
            sunflowerSmtpMailSender.send(user.getUsername(),
                    SunflowerSmtpMailSender.verificationSubject,
                    SunflowerSmtpMailSender.verificationBody + verificationLink + SunflowerSmtpMailSender.signatureLine);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public User getUserByToken(String token) {
        User user = getVerificationToken(token).getUser();
        user.setWallet(new Wallet(CustomKeyGenerator.generateWallet(), user));
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private boolean emailExist(String username) {
        User user = userRepository.findByUsername(username);

        return user != null;
    }
}
