package org.hamster.sunflower_v2.domain.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/19/2018
 */
public class CustomUserDetails implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;
    private String username;
    private String password;

    public CustomUserDetails(User user) {
        username = user.getUsername();
        password = user.getPassword();
        authorities = translate(user.getRoles());
    }

    private Collection<? extends GrantedAuthority>  translate(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            String name = role.getRole().toUpperCase();

            if(!name.startsWith("ROLE_"))
                name = "ROLE_" + name;

            authorities.add(new SimpleGrantedAuthority(name));
        }

        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
