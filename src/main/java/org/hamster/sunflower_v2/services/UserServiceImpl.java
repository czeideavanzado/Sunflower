package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.SunflowerSmtpMailSender;
import org.hamster.sunflower_v2.domain.models.*;

import org.hamster.sunflower_v2.exceptions.EmailDoesNotExistException;
import org.hamster.sunflower_v2.exceptions.EmailExistsException;
import org.hamster.sunflower_v2.exceptions.TokenDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private SunflowerSmtpMailSender sunflowerSmtpMailSender;
    private VerificationTokenRepository verificationTokenRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, SunflowerSmtpMailSender sunflowerSmtpMailSender, VerificationTokenRepository verificationTokenRepository, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.sunflowerSmtpMailSender = sunflowerSmtpMailSender;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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

        String subject = "Email Address Verification";
        String body =
                "Thank you for signing up to Sunflower! <br />" +
                "Please click the link below to verify your email address and activate your account: <br /><br />";

        String welcomingRemarks = "<br /><br />Thank you and welcome to Sunflower!<br /><br />";

        try {
            sunflowerSmtpMailSender.send(user.getUsername(),
                    subject,
                    body + verificationLink + welcomingRemarks + SunflowerSmtpMailSender.signatureLine);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public User getUserByVerificationToken(String token) {
        VerificationToken verificationToken;

        try {
            verificationToken = getVerificationToken(token);
        } catch (NullPointerException e) {
            throw new TokenDoesNotExistException("Invalid token: " + token);
        }

        return verificationToken.getUser();
    }

    @Override
    public void verifyMockUser(User user) {
        user.setWallet(new Wallet(CustomKeyGenerator.generateWallet(), user));
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void verifyUser(String token) {
        User user = verificationTokenRepository.findByToken(token).getUser();
        verificationTokenRepository.delete(verificationTokenRepository.findByToken(token));

        if (user.getWallet() != null) {
            user.setWallet(new Wallet(CustomKeyGenerator.generateWallet(), user));
        }

        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public boolean isVerificationTokenExpired(String token) {
        VerificationToken verificationToken = getVerificationToken(token);

        Calendar cal = Calendar.getInstance();

        return (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0;
    }

    @Override
    public void createPasswordResetToken(User user, String token) throws EmailDoesNotExistException {
        if (!emailExist(user.getUsername())) {
            throw new EmailDoesNotExistException("There is no account with that email address:"
                    + user.getUsername());
        }

        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);

        String resetLink = "<a href='https://localhost:8443/resetPassword?token=" + token + "'>Reset my password</a>";

        String subject = "Account Password Reset";
        String body =
                "Please click the link below to reset your password: <br /> <br />";

        String warningBody =
                "<br /> <br />Please disregard this message if you have not opted for a password reset.<br /><br />";

        try {
            sunflowerSmtpMailSender.send(user.getUsername(),
                    subject,
                    body + resetLink + warningBody + SunflowerSmtpMailSender.signatureLine);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        try {
            return getPasswordResetToken(token).getUser();
        } catch (TokenDoesNotExistException e) {
            throw new TokenDoesNotExistException("Invalid token: " + token);
        }
    }

    @Override
    public User changeUserPassword(User user, String password){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(user);
        passwordResetTokenRepository.delete(passwordResetToken);

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public boolean isPasswordResetTokenExpired(String token) {
        PasswordResetToken passwordResetToken = getPasswordResetToken(token);

        Calendar cal = Calendar.getInstance();

        return (passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0;
    }

    private boolean emailExist(String username) {
        User user = userRepository.findByUsername(username);

        return user != null;
    }
}
