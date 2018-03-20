package org.hamster.sunflower_v2.domain.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

    @CreationTimestamp
    @Column(name = "created_date")
    private java.sql.Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private java.sql.Timestamp modifiedDate;


    public Seed() {
    }

    public SeedId getId() {
        return id;
    }

    public void setId(SeedId id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
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
