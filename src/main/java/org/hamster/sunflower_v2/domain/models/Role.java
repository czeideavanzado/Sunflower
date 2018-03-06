package org.hamster.sunflower_v2.domain.models;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "role")
    private String role;

    public Role() {}

    public Role(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
