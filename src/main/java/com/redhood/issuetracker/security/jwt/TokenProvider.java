package com.redhood.issuetracker.security.jwt;

import com.redhood.issuetracker.config.properties.JWTPropertiesConfiguration;
import com.redhood.issuetracker.service.account.accounts.AccountsService;
import com.redhood.issuetracker.service.settings.SettingsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private static final String AUTHORITIES_KEY = "auth";
    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";
    private JwtParser jwtParser;
    private Key key;
    private final SettingsService settingsService;
    private final JWTPropertiesConfiguration configuration;
    private final CustomClaims claims;
    private final AccountsService accountsService;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public TokenProvider(SettingsService settingsService, JWTPropertiesConfiguration configuration, CustomClaims claims, AccountsService accountsService) {
        this.settingsService = settingsService;
        this.configuration = configuration;
        this.claims = claims;
        this.accountsService = accountsService;

        byte[] keyBytes;
        String secret = configuration.getSecret();
        keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Create JWToken from authentication details: {@code principal} , {@code credentials}
     * @param authentication not null
     * @param rememberMe {@code boolean} parameter. When {@code rememberMe} is {@code true} - increase expiration time.
     * @return {@code String} JWToken
     */
    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date validity;

        if (rememberMe) {
            validity = new Date(now + Long.parseLong(configuration.getTokenValidityForRememberMe()));
        } else if (!settingsService.findSettings().getTokenValidityTime().equals(null)){
            validity = new Date(now + settingsService.findSettings().getTokenValidityTime());
        } else {
            validity = new Date(now + Long.parseLong(configuration.getTokenValidity()));

        }

        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .compact();
    }

    /**
     * Get authorities from JWT token.
     * @param token JWT token
     * @return {@code Authentication} - get authorities from JWT token.
     */
    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Parse and validate token claims
     * @param authToken JWT token
     * @return {@code boolean} true when token is valid.
     */
    public boolean validateToken(String authToken) {
            jwtParser.parseClaimsJws(authToken);
            return true;
    }

}
