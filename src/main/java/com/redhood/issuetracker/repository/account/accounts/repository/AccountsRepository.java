package com.redhood.issuetracker.repository.account.accounts.repository;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Integer> {

    Optional<Accounts> findOneByActivationKey(String activationKey);

    Optional<Accounts> findOneByLogin(String login);

    Optional<Accounts> findOneByResetKey(String resetKey);

    Optional<Accounts> findOneWithAuthoritiesByLogin(String login);

    Optional<Accounts> findOneByEmailIgnoreCase(String email);

    Optional<Accounts> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    List<Accounts> findAllByIdNotNullAndActivatedIsTrue();

    Optional<Accounts> findRoleByLogin(String login);


}
