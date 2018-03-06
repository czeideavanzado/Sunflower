package org.hamster.sunflower_v2.domain.models;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by ONB-CZEIDE on 03/05/2018
 */
public class SeedDTO {

    @NotNull
    private BigDecimal value;

    public SeedDTO() {
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
