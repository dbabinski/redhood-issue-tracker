package com.redhood.issuetracker.service.account.permissions;

import com.redhood.issuetracker.repository.account.permissions.repository.PermissionsRepository;
import com.redhood.issuetracker.repository.account.permissions.entity.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionsServiceImpl implements PermissionsService{

    private PermissionsRepository permissionsRepository;

    @Autowired
    public PermissionsServiceImpl(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public List<Permissions> findAll() {
        return permissionsRepository.findAll();
    }

    @Override
    public Permissions findById(int id) {
        Optional<Permissions> result =  permissionsRepository.findById(id);
        Permissions permission = null;
        if(result.isPresent()) { permission = result.get(); }
        return permission;
    }

    @Override
    public void save(Permissions permissions) {
        permissionsRepository.save(permissions);
    }

    @Override
    public void deleteById(int id) {
        permissionsRepository.deleteById(id);
    }
}
