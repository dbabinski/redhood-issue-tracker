package com.redhood.issuetracker.repository.account.auth.entity;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "auth", schema = "account")
public class Auth {
    //------------------------------------------------------------------------------------------------------------------
    // Columns
    //------------------------------------------------------------------------------------------------------------------
    @Id
    @SequenceGenerator(name = "accounts.auth_id_gen", sequenceName = "accounts.auth_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Column(name = "token")
    private String token;

    @Column(name = "token_time")
    private Date tokenTime;

    @JoinColumn(name = "id_account", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Accounts idAccount;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public Auth() {}

    public Auth(String token, Date tokenTime, Accounts idAccount) {
        this.token = token;
        this.tokenTime = tokenTime;
        this.idAccount = idAccount;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Getters and Setters
    //------------------------------------------------------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Date tokenTime) {
        this.tokenTime = tokenTime;
    }

    public Accounts getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Accounts idAccount) {
        this.idAccount = idAccount;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Object
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auth auth = (Auth) o;
        return id == auth.id && Objects.equals(token, auth.token) && Objects.equals(tokenTime, auth.tokenTime) && idAccount.equals(auth.idAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, tokenTime, idAccount);
    }

    @Override
    public String toString() {
        return "com.redhood.issuetracker.account.auth.entity.Auth{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", tokenTime=" + tokenTime +
                ", idAccount=" + idAccount +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------
}
