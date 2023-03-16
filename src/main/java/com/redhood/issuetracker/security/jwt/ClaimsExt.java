package com.redhood.issuetracker.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public interface ClaimsExt extends Claims {

    public static final String TOKEN = "token";
    public static final String SCOPE = "scope";
    public static final String UUID = "uuid";
    public static final String EMAIL = "email";
    public static final String LOGIN = "login";
    public static final String AUTH_TIME = "auth_time";
    public static final String SESSION_TIME = "session_time";
    public static final String DOMAIN = "domain";
    public static final String PERMISSIONS = "permissions";

    public String getToken();

    public ClaimsExt setToken(String string);

    public String getScope();

    public ClaimsExt setScope(String string);

    public String getUUID();

    public ClaimsExt setUUID(String string);

    public String getEmail();

    public ClaimsExt setEmail(String string);

    public String getLogin();

    public ClaimsExt setLogin(String string);

    public Date getAuthTime();

    public ClaimsExt setAuthTime(Date date);

    public Integer getSessionTime();

    public ClaimsExt setSessionTime(Integer integer);

    public String getDomain();

    public ClaimsExt setDomain(String string);

    public String getPermissions();

    public ClaimsExt setPermissions(String string);

    @Override
    public Date getExpiration();

    @Override
    public Date getNotBefore();

    @Override
    public Date getIssuedAt();

}
