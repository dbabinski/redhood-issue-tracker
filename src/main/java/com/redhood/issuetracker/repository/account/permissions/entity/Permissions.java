package com.redhood.issuetracker.repository.account.permissions.entity;

import com.redhood.issuetracker.repository.account.groups.entity.Groups;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "permissions", schema = "account")
public class Permissions {
    //------------------------------------------------------------------------------------------------------------------
    // Columns
    //------------------------------------------------------------------------------------------------------------------
    @Id
    @SequenceGenerator(name = "account.permissions_id_gen", sequenceName = "account.permissions_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account.permissions_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @JoinColumn(name = "id_group", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Groups idGroup;

    @Column(name = "manage_user")
    private int manageUser;

    @Column(name = "manage_settings")
    private int manageSettings;

    @Column(name = "manage_access")
    private int manageAccess;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public Permissions() {}

    public Permissions(int id, Groups idGroup, int manageUser, int manageSettings, int manageAccess) {
        this.id = id;
        this.idGroup = idGroup;
        this.manageUser = manageUser;
        this.manageSettings = manageSettings;
        this.manageAccess = manageAccess;
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

    public Groups getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Groups idGroup) {
        this.idGroup = idGroup;
    }

    public int getManageUser() {
        return manageUser;
    }

    public void setManageUser(int manageUser) {
        this.manageUser = manageUser;
    }

    public int getManageSettings() {
        return manageSettings;
    }

    public void setManageSettings(int manageSettings) {
        this.manageSettings = manageSettings;
    }

    public int getManageAccess() {
        return manageAccess;
    }

    public void setManageAccess(int manageAccess) {
        this.manageAccess = manageAccess;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Object
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permissions that = (Permissions) o;
        return id == that.id && manageUser == that.manageUser && manageSettings == that.manageSettings && manageAccess == that.manageAccess && idGroup.equals(that.idGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idGroup, manageUser, manageSettings, manageAccess);
    }

    @Override
    public String toString() {
        return "com.redhood.issuetracker.repository.account.permissions.entity.Permissions{" +
                "id=" + id +
                ", idGroup=" + idGroup +
                ", manageUser=" + manageUser +
                ", manageSettings=" + manageSettings +
                ", manageAccess=" + manageAccess +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------

    public static final List<String> PERMISSIONS_LIST = Arrays.asList(
            "manageUser",
            "manageSettings",
            "manageAccess"
    );
}