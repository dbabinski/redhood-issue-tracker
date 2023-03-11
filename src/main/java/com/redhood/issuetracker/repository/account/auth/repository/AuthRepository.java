package com.redhood.issuetracker.repository.account.auth.repository;

import com.redhood.issuetracker.repository.account.auth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Integer> {
}
