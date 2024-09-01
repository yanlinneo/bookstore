package com.neoyanlin.bookstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
    	String username = jwt.getSubject();

        // Extract roles from JWT claims
        List<String> roles = jwt.getClaimAsStringList("roles");

        // Convert roles to Spring Security GrantedAuthorities
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Create custom authentication token
        return new CustomAuthenticationToken(username, authorities);
    }
}