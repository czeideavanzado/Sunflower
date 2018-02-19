package org.hamster.sunflower_v2.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "enabled")
    private boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(String pUsername, String pPassword, String pFirstName, String pLastName) {
        username = pUsername;
        password = pPassword;
        first_name = pFirstName;
        last_name = pLastName;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setFirstName(String pFirstName) {
        first_name = pFirstName;
    }

    public void setLastName(String pLastName) {
        last_name = pLastName;
    }

    public void setPassword(String pPassword) {
        password = pPassword;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }

    public void setRoles(Set<Role> pRoles) {
        roles = pRoles;
    }

    public void setEnabled(Boolean pCondition) {
        enabled = pCondition;
    }
}
