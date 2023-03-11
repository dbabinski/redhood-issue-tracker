package com.redhood.issuetracker.repository.account.accounts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redhood.issuetracker.repository.account.AbstractEntity;
import com.redhood.issuetracker.repository.account.groups.entity.Groups;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Component
@Entity
@Table(name = "accounts", schema = "account")
public class Accounts extends AbstractEntity<Integer> implements Serializable, UserDetails {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Columns
    //------------------------------------------------------------------------------------------------------------------
    @Id
    @SequenceGenerator(name = "accounts.account_id_gen", sequenceName = "accounts.account_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Pattern(regexp = LOGIN_REGEX)
    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private Boolean activated;

    @Column(name = "blocked")
    private Instant blocked;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastname;

    @JsonIgnore
    @Size(min = 6, max = 128)
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "activation_key")
    private String activationKey;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JoinColumn(name = "id_group", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Groups idGroup;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public Accounts() {}

    public Accounts(Integer id, String login, String email, Boolean activated, Instant blocked, String name, String lastname, String password, String activationKey, String resetKey, Instant resetDate, Groups idGroup) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.activated = activated;
        this.blocked = blocked;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.activationKey = activationKey;
        this.resetKey = resetKey;
        this.resetDate = resetDate;
        this.idGroup = idGroup;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Getters and Setters
    //------------------------------------------------------------------------------------------------------------------
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean active) {
        this.activated = active;
    }

    public Instant getBlocked() {
        return blocked;
    }

    public void setBlocked(Instant blocked) {
        this.blocked = blocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String surname) {
        this.lastname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public Groups getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Groups idGroup) {
        this.idGroup = idGroup;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // UserDetails interface
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(getIdGroup().getGroupName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return activated;
    }

    @Override
    public boolean isAccountNonLocked() {
        return activated;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return activated;
    }

    @Override
    public boolean isEnabled() {
        return activated;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Object
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Accounts accounts = (Accounts) o;
        return id == accounts.id && Objects.equals(login, accounts.login) && Objects.equals(email, accounts.email) && Objects.equals(activated, accounts.activated) && Objects.equals(blocked, accounts.blocked) && Objects.equals(name, accounts.name) && Objects.equals(lastname, accounts.lastname) && Objects.equals(activationKey, accounts.activationKey) && Objects.equals(resetKey, accounts.resetKey) && Objects.equals(resetDate, accounts.resetDate) && Objects.equals(idGroup, accounts.idGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, email, activated, blocked, name, lastname, activationKey, resetKey, resetDate, idGroup);
    }

    @Override
    public String toString() {
        return "com.redhood.issuetracker.account.accounts.entity.Accounts{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", active=" + activated +
                ", blocked=" + blocked +
                ", name='" + name + '\'' +
                ", lastName='" + lastname + '\'' +
                ", activationKey='" + activationKey + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", resetDate='" + resetDate + '\'' +
                ", idGroup=" + idGroup +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------
}
