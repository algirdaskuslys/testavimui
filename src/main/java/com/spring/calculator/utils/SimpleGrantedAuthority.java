package com.spring.calculator.utils;

import org.springframework.security.core.GrantedAuthority;

public final class SimpleGrantedAuthority implements GrantedAuthority {

    private final String role;

    public SimpleGrantedAuthority(String authority) {
        role = authority;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
