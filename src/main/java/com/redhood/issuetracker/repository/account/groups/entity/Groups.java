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
    @SequenceGenerator(name = "account.groups_id_gen", sequenceName = "account.groups_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account.groups_id_gen")
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "defaults")
    private boolean defaults;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    public Groups() {}

    public Groups(String groupName, boolean defaults) {
        this.groupName = groupName;
        this.defaults = defaults;
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

    public boolean isDefaults() {
        return defaults;
    }

    public void setDefaults(boolean defaults) {
        this.defaults = defaults;
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
        return id == groups.id && defaults == groups.defaults && Objects.equals(groupName, groups.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, defaults);
    }

    @Override
    public String toString() {
        return "com.redhood.issuetracker.account.groups.entity.Groups{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", defaults='" + defaults + '\'' +
                '}';
    }
    //------------------------------------------------------------------------------------------------------------------
}