package com.redhood.issuetracker.service.account.permissions;



import com.redhood.issuetracker.repository.account.permissions.entity.Permissions;

import java.util.List;

public interface PermissionsService {
    public List<Permissions> findAll();

    public Permissions findById(int id);

    public void save(Permissions permissions);

    public void deleteById(int id);
}
