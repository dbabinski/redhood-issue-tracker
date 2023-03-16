package com.redhood.issuetracker.security.jwt;

import com.redhood.issuetracker.config.properties.JWTPropertiesConfiguration;
import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import com.redhood.issuetracker.repository.account.permissions.entity.Permissions;
import com.redhood.issuetracker.service.account.accounts.AccountsService;
import com.redhood.issuetracker.service.account.permissions.PermissionsServiceImpl;
import com.redhood.issuetracker.service.account.permissions.handler.PermissionsHandler;
import com.redhood.issuetracker.service.settings.SettingsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
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
    private final PermissionsServiceImpl permissionsService;

    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public TokenProvider(SettingsService settingsService, JWTPropertiesConfiguration configuration, CustomClaims claims, AccountsService accountsService, PermissionsServiceImpl permissionsService) {
        this.settingsService = settingsService;
        this.configuration = configuration;
        this.claims = claims;
        this.accountsService = accountsService;
        this.permissionsService = permissionsService;

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
        Permissions permissions = getPermissions(accountsService.findByLogin(authentication.getName()));

        claims.put(CustomClaims.SCOPE, authorities);
        claims.put(CustomClaims.LOGIN, authentication.getName());
        claims.put(CustomClaims.PERMISSIONS, getPermissionsJSON(permissions).toMap());
        claims.put(CustomClaims.EMAIL, accountsService.findByLogin(authentication.getName()).getEmail());

        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(claims)
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

    /**
     * Getting permissions from assigned account group.
     * @param account account data
     * @return {@link Permissions}, every field is update via PERMISSIONS_LIST..
     */
    private Permissions getPermissions(Accounts account){
        if (account != null) {

            Permissions permissions = new Permissions();
            List<Permissions> permissionsList = new ArrayList<>();
            Permissions accountPermissions = permissionsService.findOneByIdGroup(account.getIdGroup().getId()).orElse(new Permissions());
            permissionsList.add(accountPermissions);


            for (String permissionName : Permissions.PERMISSIONS_LIST) {
                try {
                    int result = 0;
                    for (Permissions permission : permissionsList) {
                        Field field = null;
                        field = Permissions.class.getDeclaredField(permissionName);
                        field.setAccessible(true);
                        result |= field.getInt(permission);

                    }
                    Field field = null;
                    field = Permissions.class.getDeclaredField(permissionName);
                    field.setAccessible(true);
                    field.setInt(permissions, result);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    return new Permissions();
                }
            }
            return permissions;
        }
        return new Permissions();
    }

    /**
     * Parse permissoins from int value, to "readeable" format read/ add/ delete/ update/
     * @param permissions
     * @return {@code JSON} with values of permissions.
     * Example: permission - {@code manageAccess} equals {@code 15} int,
     *          means, the permissions are as follows {@code read} - true, {@code add} - true, {@code delete - true}, {@code update} - true.
     */
    private JSONObject getPermissionsJSON(Permissions permissions) {
        JSONObject jsonb = new JSONObject();
        try {
            for (String permision : Permissions.PERMISSIONS_LIST) {
                Field field = Permissions.class.getDeclaredField(permision);
                field.setAccessible(true);
                PermissionsHandler permissionsH = new PermissionsHandler(field.getInt(permissions));
                jsonb.put(permision, new JSONObject()
                        .put("read", permissionsH.isRead())
                        .put("add", permissionsH.isAdd())
                        .put("delete", permissionsH.isDelete())
                        .put("update", permissionsH.isUpdate()));
            }
            return jsonb;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return jsonb.put("permission_is_empty", new JSONObject());
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}
