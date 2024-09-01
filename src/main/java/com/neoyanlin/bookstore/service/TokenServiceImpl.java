package com.neoyanlin.bookstore.service;

import java.security.interfaces.RSAPrivateKey;
import java.sql.Date;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenServiceImpl implements TokenService {
	private final RSAPrivateKey privateKey;
	
	@Autowired
    public TokenServiceImpl(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }
 
	@Override
	public String generateToken(Authentication authentication) {
		// Extract username from authentication
	    String username = authentication.getName();
	    
	    // Extract roles from authentication and convert them to strings
	    Collection<String> roles = authentication.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toList());

		return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + 900000)) // 15 minute expiration
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
		
	}
}
