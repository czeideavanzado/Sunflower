package org.hamster.sunflower_v2.domain.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
public class SeedDTO {

    @NotNull
    @NotEmpty
    private String serialCode;

    @NotNull
    @NotEmpty
    private String serialPin;

    private BigDecimal value;

    public SeedDTO() {
    }

    public SeedDTO(BigDecimal value) {
        this.value = value;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serial_code) {
        this.serialCode = serial_code;
    }

    public String getSerialPin() {
        return serialPin;
    }

    public void setSerialPin(String serial_pin) {
        this.serialPin = serial_pin;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
