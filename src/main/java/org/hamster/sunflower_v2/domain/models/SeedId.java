package org.hamster.sunflower_v2.domain.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by ONB-CZEIDE on 03/06/2018
 */
@Embeddable
public class SeedId implements Serializable {

    @Column(name = "serial_code")
    private String serialCode;

    @Column(name = "serial_pin")
    private String serialPin;

    public SeedId() {
    }

    public SeedId(String serialCode, String serialPin) {
        this.serialCode = serialCode;
        this.serialPin = serialPin;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public void setSerialPin(String serialPin) {
        this.serialPin = serialPin;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public String getSerialPin() {
        return serialPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeedId)) return false;
        SeedId seedId = (SeedId) o;
        return Objects.equals(serialCode, seedId.serialCode) &&
                Objects.equals(serialPin, seedId.serialPin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(serialCode, serialPin);
    }
}
