package com.redhood.issuetracker.repository.account.permissions.entity;

import com.redhood.issuetracker.repository.account.groups.entity.Groups;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "permissions", schema = "account")
public class Permissions {
    //------------------------------------------------------------------------------------------------------------------
    // Columns
    //------------------------------------------------------------------------------------------------------------------
    @Id
    @SequenceGenerator(name = "accounts.permissions_id_gen", sequenceName = "accounts.permissions_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @JoinColumn(name = "id_group", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Groups idGroup;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public Permissions() {}

    public Permissions(Groups idGroup) {
        this.idGroup = idGroup;
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
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Object
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permissions that = (Permissions) o;
        return id == that.id && Objects.equals(idGroup, that.idGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idGroup);
    }

    @Override
    public String toString() {
        return "com.redhood.issuetracker.account.permissions.entity.Permissions{" +
                "id=" + id +
                ", idGroup=" + idGroup +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------
}