package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Role;
import org.hamster.sunflower_v2.domain.models.User;
import org.hamster.sunflower_v2.domain.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/26/2018
 */
@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> userOptional = userRepository.findByUsername(username);
//
//        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

//        return userOptional
//                .map(CustomUserDetails::new)
//                .get();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(),
                        user.isEnabled(), true,
                        true, user.isAccountNonLocked(),
                        getAuthorities(user.getRoles()));
    }

    private static Set<GrantedAuthority> getAuthorities (Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }
}
