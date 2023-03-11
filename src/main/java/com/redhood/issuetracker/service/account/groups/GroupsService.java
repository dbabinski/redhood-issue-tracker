package com.redhood.issuetracker.service.account.groups;

import com.redhood.issuetracker.repository.account.groups.entity.Groups;

import java.util.List;

public interface GroupsService {

    public List<Groups> findAll();

    public Groups findById(int id);

    public void save(Groups groups);

    public void deleteById(int id);
}