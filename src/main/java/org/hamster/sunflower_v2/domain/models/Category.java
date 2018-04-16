package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;

/**
 * Created by ONB-CZEIDE on 04/15/2018
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
