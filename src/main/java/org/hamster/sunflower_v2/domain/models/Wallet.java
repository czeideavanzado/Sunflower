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

    @Column(name = "seeds")
    private BigDecimal seeds;

    public Wallet() {
    }

    public Wallet(String id) {
        setId(id);
        seeds = BigDecimal.ZERO;
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
}
