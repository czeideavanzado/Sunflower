package org.hamster.sunflower_v2.domain.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_attempts")
public class UserAttempt {

    @Id
    @GeneratedValue(generator = "user_attempt_sequence")
    @GenericGenerator(
            name = "user_attempt_sequence",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "USER_ATTEMPT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User logger;

    @Column(name = "attempts")
    private int attempt = 0;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private java.sql.Timestamp modifiedDate;

    public UserAttempt() {
    }

    public UserAttempt(User logger) {
        this.logger = logger;
    }

    public User getLogger() {
        return logger;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public int getAttempt() {
        return attempt;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }
}
