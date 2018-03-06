package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
@Entity
@Table(name = "seeds")
public class Seed {

    @Id
    @Column(name = "serialCode")
    private String serialCode;

    @Column(name = "serialPin")
    private String serialPin;

    @Column(name = "value")
    private BigDecimal value;

    public Seed() {
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public void setSerialPin(String serialPin) {
        this.serialPin = serialPin;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public String getSerialPin() {
        return serialPin;
    }

    public BigDecimal getValue() {
        return value;
    }
}
