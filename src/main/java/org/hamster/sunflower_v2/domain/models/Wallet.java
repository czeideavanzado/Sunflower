package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by ONB-CZEIDE on 03/06/2018
 */
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @Column(name = "id")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "seeds")
    private BigDecimal seeds;

    public Wallet() {
    }

    public Wallet(String id, User user) {
        setId(id);
        seeds = BigDecimal.ZERO;
        owner = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSeeds(BigDecimal seeds) {
        this.seeds = seeds;
    }

    public BigDecimal getSeeds() {
        return seeds;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
