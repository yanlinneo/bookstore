package com.neoyanlin.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neoyanlin.bookstore.exception.TokenResponse;
import com.neoyanlin.bookstore.pojo.LoginForm;
import com.neoyanlin.bookstore.service.TokenServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final TokenServiceImpl tokenService;
	
	private AuthenticationManager authenticationManager;
	
	@Autowired
	public AuthController(TokenServiceImpl tokenService, AuthenticationManager authenticationManager) {
		this.tokenService = tokenService;
		this.authenticationManager = authenticationManager;
	}
	
	@PostMapping("/token")
	public ResponseEntity<TokenResponse> token(@RequestBody LoginForm loginRequest) {
		// Authenticate the user

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
		String token = tokenService.generateToken(authentication);
		TokenResponse tokenResponse = new TokenResponse(token);
		return ResponseEntity.ok((tokenResponse));

	}
}
