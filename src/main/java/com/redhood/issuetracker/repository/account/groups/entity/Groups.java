package com.redhood.issuetracker.repository.account.groups.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "groups", schema = "account")
public class Groups {
    //------------------------------------------------------------------------------------------------------------------
    // Columns
    //------------------------------------------------------------------------------------------------------------------
    @Id
    @SequenceGenerator(name = "accounts.groups_id_gen", sequenceName = "accounts.groups_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Column(name = "group_name")
    private String groupName;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public Groups() {}

    public Groups(String groupName) {
        this.groupName = groupName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Object
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Groups groups = (Groups) o;
        return id == groups.id && Objects.equals(groupName, groups.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName);
    }

    @Override
    public String toString() {
        return "com.redhood.issuetracker.account.groups.entity.Groups{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------
}