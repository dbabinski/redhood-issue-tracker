package com.redhood.issuetracker.service.account.dto;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import com.redhood.issuetracker.repository.account.accounts.repository.AccountsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String login;

    private String password;

    private boolean rememberMe;

    public UserDTO() {
    }

    public UserDTO(Accounts accounts) {
        this.id = accounts.getId();
        this.login = accounts.getLogin();
        this.password = accounts.getPassword();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", rememberMe='" + rememberMe + '\'' +
                '}';
    }


}
