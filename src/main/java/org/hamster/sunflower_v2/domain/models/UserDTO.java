package org.hamster.sunflower_v2.domain.models;

import org.hamster.sunflower_v2.domain.constraints.PasswordMatches;
import org.hamster.sunflower_v2.domain.constraints.ValidEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
@PasswordMatches
public class UserDTO {

    @NotNull
    @NotEmpty
    private String first_name;

    @NotNull
    @NotEmpty
    private String last_name;

//    @ValidEmail(min = 6, message = "Please enter a valid email.")
    @Email
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String passwordConfirm;

    public UserDTO() {

    }

    public UserDTO(String username, String password, String passwordConfirm, String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
