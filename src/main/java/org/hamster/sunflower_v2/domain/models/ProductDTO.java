package org.hamster.sunflower_v2.domain.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
public class ProductDTO {

    @NotNull
    @NotEmpty
    private String name;

    private BigDecimal price;

    private String description;
    private String photo;

    private Long seller_id;

    private Long category_id;

    public ProductDTO() {
    }

    public ProductDTO(String name, BigDecimal price, String description, String photo, Long seller_id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.photo = photo;
        this.seller_id = seller_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSeller_id(Long seller_id) {
        this.seller_id = seller_id;
    }

    public Long getSeller_id() {
        return seller_id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
}
