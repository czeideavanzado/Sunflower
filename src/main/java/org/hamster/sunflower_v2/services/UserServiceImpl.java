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
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TransactionRepository transactionRepository;
    private PasswordEncoder passwordEncoder;

    private SunflowerSmtpMailSender sunflowerSmtpMailSender;
    private VerificationTokenRepository verificationTokenRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private UserAttemptRepository userAttemptRepository;

    private final int MAX_ATTEMPTS = 3;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, TransactionRepository transactionRepository, PasswordEncoder passwordEncoder, SunflowerSmtpMailSender sunflowerSmtpMailSender, VerificationTokenRepository verificationTokenRepository, PasswordResetTokenRepository passwordResetTokenRepository, UserAttemptRepository userAttemptRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
        this.sunflowerSmtpMailSender = sunflowerSmtpMailSender;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userAttemptRepository = userAttemptRepository;
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

        if(!StringUtils.isEmpty(accountDto.getImage())) {
            user.setImage(accountDto.getImage());
        }

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

    @Transactional
    @Override
    public String updateFailedAttempt(String username) {
        if (!emailExist(username)){
            return "email";
        }

        UserAttempt userAttempt = getUserAttemptByUsername(username);

        if (userAttempt.getExpiryDate() != null) {
            if (accountLockedExpiration(userAttempt)) {
                resetFailedAttempt(username);
                unlockUser(username);
            }
        }

        if (userAttempt.getAttempt() < 3) {
            userAttempt.setAttempt(userAttempt.getAttempt() + 1);

            if (userAttempt.getAttempt() == 3) {
                User user = userAttempt.getLogger();
                user.setAccountNonLocked(false);
                userRepository.save(user);

                userAttempt.setExpiryDate();
            }

            userAttemptRepository.save(userAttempt);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        String dateString = "";

        if (userAttempt.getExpiryDate() != null) {
            dateString = sdf.format(userAttempt.getExpiryDate());
        }

        return userAttempt.getAttempt() + 1 > 3 ? "locked&expiration=" + dateString : "incorrect&attempt=" + userAttempt.getAttempt();
    }

    private void unlockUser(String username) {
        UserAttempt userAttempt = getUserAttemptByUsername(username);
        
        User user = userAttempt.getLogger();
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

    @Override
    public void resetFailedAttempt(String username) {
        UserAttempt userAttempt = getUserAttemptByUsername(username);

        userAttempt.setAttempt(0);
        userAttemptRepository.save(userAttempt);
    }

    @Override
    public UserAttempt getUserAttemptByUsername(String username) {
        User user = userRepository.findByUsername(username);

        UserAttempt userAttempt = userAttemptRepository.findByLogger(user);

        if (userAttempt == null) {
            return new UserAttempt(user);
        }

        return userAttempt;
    }

    private boolean emailExist(String username) {
        User user = userRepository.findByUsername(username);

        return user != null;
    }

    @Override
    public void disableBuyer(Long id) {
        User user1 = userRepository.findOne(id);
        Set<Role> roles = user1.getRoles();
        for (Role role : roles) {
            if(role.getRole().equalsIgnoreCase("BUYER"))
                roles.remove(role);
        }
        user1.setRoles(roles);
        userRepository.save(user1);


    }

    @Override
    public void disableSeller(Long id) {
        User user1 = userRepository.findOne(id);
        Set<Role> roles = user1.getRoles();
        for (Role role : roles) {
            if(role.getRole().equalsIgnoreCase("SELLER"))
                roles.remove(role);
        }
        user1.setRoles(roles);
        userRepository.save(user1);
    }

    @Override
    public void disableMod(Long id) {
        User user1 = userRepository.findOne(id);
        Set<Role> roles = user1.getRoles();
        for (Role role : roles) {
            if(role.getRole().equalsIgnoreCase("MODERATOR"))
                roles.remove(role);
        }
        user1.setRoles(roles);
        userRepository.save(user1);
    }

    @Override
    public void enableBuyer(Long id) {
        User user1 = userRepository.findOne(id);
        Role role = roleRepository.findByRole("BUYER");
        Set<Role> roles = user1.getRoles();
        roles.add(role);
        user1.setRoles(roles);
        userRepository.save(user1);
    }

    @Override
    public void enableSeller(Long id) {
        User user1 = userRepository.findOne(id);
        Role role = roleRepository.findByRole("SELLER");
        Set<Role> roles = user1.getRoles();
        roles.add(role);
        user1.setRoles(roles);
        userRepository.save(user1);
    }

    @Override
    public void enableMod(Long id) {
        User user1 = userRepository.findOne(id);
        Role role = roleRepository.findByRole("MODERATOR");
        Set<Role> roles = user1.getRoles();
        roles.add(role);
        user1.setRoles(roles);
        userRepository.save(user1);
    }

    private boolean accountLockedExpiration(UserAttempt userAttempt) {
        Calendar cal = Calendar.getInstance();

        return (userAttempt.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0;
    }
}
