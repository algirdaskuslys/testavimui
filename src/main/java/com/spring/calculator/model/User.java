package com.spring.calculator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    public enum UserRole {
        ADMIN,
        USER
    }

    public String getAuthorities() {
        return role.toString();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Transient
    private String passwordConfirm;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Number> calculationsList;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                '}';
    }
}
