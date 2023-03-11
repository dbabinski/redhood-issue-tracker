package com.redhood.issuetracker.service.account.groups;

import com.redhood.issuetracker.repository.account.groups.repository.GroupsRepository;
import com.redhood.issuetracker.repository.account.groups.entity.Groups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GroupsServiceImpl implements GroupsService {

    private GroupsRepository groupsRepository;

    @Autowired
    public GroupsServiceImpl (GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @Override
    @Transactional
    public List<Groups> findAll() {
        return groupsRepository.findAll();
    }

    @Override
    public Groups findById(int id) {
        Optional<Groups> result = groupsRepository.findById(id);
        Groups group = null;
        if(result.isPresent()) { group = result.get(); }
        return group;
    }

    @Override
    public void save(Groups groups) {
        groupsRepository.save(groups);
    }

    @Override
    public void deleteById(int id) {
        groupsRepository.deleteById(id);
    }

}