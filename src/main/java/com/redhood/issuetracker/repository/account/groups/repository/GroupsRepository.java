package com.redhood.issuetracker.repository.account.groups.repository;

import com.redhood.issuetracker.repository.account.groups.entity.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupsRepository extends JpaRepository<Groups, Integer> {

    Optional<Groups> findOneByGroupName(String groupName);

}