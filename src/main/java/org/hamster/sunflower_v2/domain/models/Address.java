package org.hamster.sunflower_v2.domain.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by ONB-CZEIDE on 03/19/2018
 */
@Embeddable
public class Address {

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "house_details")
    private String houseDetails;

    @Column(name = "building")
    private String building;

    @Column(name = "street")
    private String street;

    @Column(name = "subdivision")
    private String subdivision;

    @Column(name = "barangay")
    private String barangay;

    @Column(name = "landmark")
    private String landmark;

    public Address() {
    }

    public Address(String province, String city, String houseDetails, String building, String street, String subdivision, String barangay, String landmark) {
        this.province = province;
        this.city = city;
        this.houseDetails = houseDetails;
        this.building = building;
        this.street = street;
        this.subdivision = subdivision;
        this.barangay = barangay;
        this.landmark = landmark;
    }
}
