package com.redhood.issuetracker.service.account.permissions;

import com.redhood.issuetracker.repository.account.groups.entity.Groups;
import com.redhood.issuetracker.repository.account.groups.repository.GroupsRepository;
import com.redhood.issuetracker.repository.account.permissions.repository.PermissionsRepository;
import com.redhood.issuetracker.repository.account.permissions.entity.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionsServiceImpl implements PermissionsService{
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private PermissionsRepository permissionsRepository;
    private GroupsRepository groupsRepository;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    public PermissionsServiceImpl(PermissionsRepository permissionsRepository, GroupsRepository groupsRepository) {
        this.permissionsRepository = permissionsRepository;
        this.groupsRepository = groupsRepository;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // PermissionsService interface
    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<Permissions> findAll() {
        return permissionsRepository.findAll();
    }

    @Override
    public Permissions findById(int id) {
        Optional<Permissions> result =  permissionsRepository.findById(id);
        Permissions permission = null;
        if(result.isPresent()) {
            permission = result.get();
        }
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

    @Override
    public Optional<Permissions> findOneByIdGroup(int idGroup) {
        Groups group = groupsRepository.findById(idGroup).get();
        Optional<Permissions> result = permissionsRepository.findOneByIdGroup(group);
        return result;

    }
    //------------------------------------------------------------------------------------------------------------------

}
