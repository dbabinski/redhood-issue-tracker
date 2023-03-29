package com.redhood.issuetracker.web.rest.account.accounts;

import com.redhood.issuetracker.security.jwt.ClaimsExt;
import com.redhood.issuetracker.security.jwt.TokenProvider;
import com.redhood.issuetracker.service.account.dto.UserDTO;
import com.redhood.issuetracker.web.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthREST {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private final Logger log = LoggerFactory.getLogger(AuthREST.class);
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ClaimsExt claims;
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------------------------------
    public AuthREST(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, ClaimsExt claims) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.claims = claims;
    }
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // REST Controller methods - GET
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // REST Controller methods - POST
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@code GET /api/auth/authenticate} : Generate token for authenticate user with given details provided by
     * {@link UserDTO} model. Add to {@code httpHeaders} JWToken.
     * @param userDTO contains details: {@code username}, {@code password} and {@code rememberMe}
     * @return the {@link ResponseEntity} with status {@code 200(OK)}.
     */
    @PostMapping("/authenticate")
    public ResponseEntity authorize(@Valid @RequestBody UserDTO userDTO) {
        Map<String, String> map = new HashMap<>();
        //TODO: custom claims to rest controller

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDTO.getLogin(),
            userDTO.getPassword()
        );
        if (authenticationToken.getCredentials() == null || authenticationToken.getPrincipal() == null){
            return ResponseHandler.generateResponseJWT("Please fill login details! Make sure your login details are correct", HttpStatus.UNAUTHORIZED, map);
        }

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, userDTO.isRememberMe());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + jwt);

            return ResponseHandler.generateResponseJWT(httpHeaders, "Successfully create auth token.", HttpStatus.OK, jwt, claims);
        } catch (AuthenticationException e) {
            log.debug("Bad credentials!");

            return ResponseHandler.generateResponseJWT("Bad credentials! Make sure your login details are correct", HttpStatus.UNAUTHORIZED, map);
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // REST Controller methods - PUT
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller methods - DELETE
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------
}
