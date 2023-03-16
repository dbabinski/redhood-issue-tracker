package com.redhood.issuetracker.repository.account.permissions.repository;

import com.redhood.issuetracker.repository.account.groups.entity.Groups;
import com.redhood.issuetracker.repository.account.permissions.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
    Optional<Permissions> findOneByIdGroup(Groups idGroup);
}
