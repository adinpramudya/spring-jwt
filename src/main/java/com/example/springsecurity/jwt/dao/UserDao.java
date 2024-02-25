package com.example.springsecurity.jwt.dao;

import com.example.springsecurity.jwt.entities.UserAuthority;

import java.util.Objects;
import java.util.Set;


public class UserDao {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<UserAuthority> userAuthorities;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserAuthority> getUserAuthoryties() {
        return userAuthorities;
    }

    public void setUserAuthoryties(Set<UserAuthority> userAuthoryties) {
        this.userAuthorities = userAuthoryties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDao userDto = (UserDao) o;
        return Objects.equals(email, userDto.email) && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

