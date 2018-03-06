package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
@Entity
@Table(name = "seeds")
public class Seed {

    @EmbeddedId
    private SeedId id;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "active")
    private boolean active;

    public Seed() {
    }

    public void setId(SeedId id) {
        this.id = id;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isActive() {
        return active;
    }
}
