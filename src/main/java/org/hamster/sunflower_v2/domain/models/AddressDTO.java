package org.hamster.sunflower_v2.domain.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by ONB-CZEIDE on 03/19/2018
 */
public class AddressDTO {

    @NotNull
    @NotEmpty
    private String province;

    @NotNull
    @NotEmpty
    private String city;

    @NotNull
    @NotEmpty
    private String houseDetails;

    private String building;

    @NotNull
    @NotEmpty
    private String street;

    private String subdivision;

    private String barangay;

    private String landmark;

    public AddressDTO() {
    }

    public AddressDTO(String province, String city, String houseDetails, String building, String street, String subdivision, String barangay, String landmark) {
        this.province = province;
        this.city = city;
        this.houseDetails = houseDetails;
        this.building = building;
        this.street = street;
        this.subdivision = subdivision;
        this.barangay = barangay;
        this.landmark = landmark;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouseDetails() {
        return houseDetails;
    }

    public void setHouseDetails(String houseDetails) {
        this.houseDetails = houseDetails;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
