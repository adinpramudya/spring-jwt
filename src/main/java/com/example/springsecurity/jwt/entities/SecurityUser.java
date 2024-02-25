package com.example.springsecurity.jwt.entities;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SecurityUser implements UserDetails {

    private static final long serialVersionUID = -6690946490872875352L;

    @Autowired
    User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getUserAuthorities() == null || user.getUserAuthorities().isEmpty()) {
            return Collections.emptyList();
        }

        return user.getUserAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().toString()))
                .collect(Collectors.toList());
    }
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}




















