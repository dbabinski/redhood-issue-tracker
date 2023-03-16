package com.redhood.issuetracker.security.jwt;

import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
public class CustomClaims extends DefaultClaims implements Principal, ClaimsExt {

    public CustomClaims() {
    }

    public CustomClaims(Map<String, ?> map) {
        super(map);
    }

    public CustomClaims(Set<Entry<String, Object>> set) {
        if (set != null) {
            set.forEach(entry -> {
                put(entry.getKey(), entry.getValue());
            });
        }
    }

    @Override
    public String getToken() {
        return super.get(TOKEN, String.class);
    }

    @Override
    public ClaimsExt setToken(String token) {
        super.put(TOKEN, token);
        return this;
    }

    @Override
    public String getScope() {
        return super.get(SCOPE, String.class);
    }

    @Override
    public ClaimsExt setScope(String scope) {
        super.put(SCOPE, scope);
        return this;
    }

    @Override
    public String getUUID() {
        return super.get(UUID, String.class);
    }

    @Override
    public ClaimsExt setUUID(String uuid) {
        super.put(UUID, uuid);
        return this;
    }

    @Override
    public String getEmail() {
        return super.get(EMAIL, String.class);
    }

    @Override
    public ClaimsExt setEmail(String email) {
        super.put(EMAIL, email);
        return this;
    }

    @Override
    public String getLogin() {
        return super.get(LOGIN, String.class);
    }

    @Override
    public ClaimsExt setLogin(String login) {
        super.put(LOGIN, login);
        return this;
    }

    @Override
    public Date getAuthTime() {
        return super.get(AUTH_TIME, Date.class);
    }

    @Override
    public ClaimsExt setAuthTime(Date date) {
        super.put(AUTH_TIME, date);
        return this;
    }

    @Override
    public Integer getSessionTime() {
        return super.get(SESSION_TIME, Integer.class);
    }

    @Override
    public ClaimsExt setSessionTime(Integer sessionTime) {
        super.put(SESSION_TIME, sessionTime);
        return this;
    }

    @Override
    public String getDomain() {
        return super.get(DOMAIN, String.class);
    }

    @Override
    public ClaimsExt setDomain(String domain) {
        super.put(DOMAIN, domain);
        return this;
    }

    @Override
    public String getPermissions() {
        return super.get(PERMISSIONS, String.class);
    }

    @Override
    public ClaimsExt setPermissions(String permissions) {
        super.put(PERMISSIONS, permissions);
        return this;
    }

    @Override
    public String getName() {
        return getUUID();
    }
}
