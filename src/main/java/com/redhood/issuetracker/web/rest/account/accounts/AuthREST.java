package com.redhood.issuetracker.web.rest.account.accounts;

import com.redhood.issuetracker.security.jwt.CustomClaims;
import com.redhood.issuetracker.security.jwt.TokenProvider;
import com.redhood.issuetracker.service.account.dto.UserDTO;
import com.redhood.issuetracker.web.utils.ResponseHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CustomClaims claims;

    public AuthREST(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, CustomClaims claims) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.claims = claims;
    }

    @PostMapping("/authenticate")
    public ResponseEntity authorize(@Valid @RequestBody UserDTO userDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDTO.getLogin(),
            userDTO.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, userDTO.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);

        Map<String, String> map = new HashMap<>();


        return ResponseHandler.generateResponseJWT(httpHeaders, "Successfully create auth token.", HttpStatus.OK, jwt, claims);
    }



}
