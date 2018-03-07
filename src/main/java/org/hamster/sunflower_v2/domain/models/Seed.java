package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

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

    public SeedId getId() {
        return id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seed)) return false;
        Seed seed = (Seed) o;
        return Objects.equals(id, seed.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Seed{" +
                "id=" + id +
                ", value=" + value +
                ", active=" + active +
                '}';
    }
}
