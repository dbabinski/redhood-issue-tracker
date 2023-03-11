package com.redhood.issuetracker.repository.account.permissions.repository;

import com.redhood.issuetracker.repository.account.permissions.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
}
