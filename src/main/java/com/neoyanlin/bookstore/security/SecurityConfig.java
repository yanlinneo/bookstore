package com.neoyanlin.bookstore.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.neoyanlin.bookstore.service.CustomAuthenticationConverter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;


@Configuration
public class SecurityConfig {
	
	private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;
    
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public SecurityConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
	
	@Bean
	public UserDetailsManager userDetailsManager(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf->csrf.disable());
		
		// Disable CSRF protection
        http.csrf(AbstractHttpConfigurer::disable);

        // Session management configuration
        http.sessionManagement(sessionManagementConfigurer ->
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
		
		http.authorizeHttpRequests(configurer -> 
			configurer.requestMatchers(HttpMethod.POST, "/auth/token").permitAll()
				.requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")
	            .anyRequest().authenticated()	            
			)
			.oauth2ResourceServer(oauth2 -> oauth2
	        	.jwt(jwt -> jwt.jwtAuthenticationConverter(new CustomAuthenticationConverter())
	        )
		);

        return http.build();
	}
	
	
	//temp
//	@Bean
//	public JwtAuthenticationConverter jwtAuthenticationConverter() {
//	    final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//	    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // defaults to "scope" or "scp"
//	    grantedAuthoritiesConverter.setAuthorityPrefix(""); // defaults to "SCOPE_"
//
//	    final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//	    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//	    return jwtAuthenticationConverter;
//	}
	
	@Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
    
}

