package org.hamster.sunflower_v2.domain.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ONB-CZEIDE on 04/09/2018
 */
@Entity
@Table(name = "password_tokens")
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 2;

    @Id
    @GeneratedValue(generator = "password_token_sequence")
    @GenericGenerator(
            name = "password_token_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PASSWORD_TOKEN_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public User getUser() {
        return user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
}
