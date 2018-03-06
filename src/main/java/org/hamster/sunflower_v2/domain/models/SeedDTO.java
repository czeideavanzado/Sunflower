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
    private String serial_code;

    @NotNull
    @NotEmpty
    private String serial_pin;

    private BigDecimal value;

    public SeedDTO() {
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public void setSerial_pin(String serial_pin) {
        this.serial_pin = serial_pin;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public String getSerial_pin() {
        return serial_pin;
    }

    public BigDecimal getValue() {
        return value;
    }
}
