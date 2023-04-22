package com.redhood.issuetracker.service.account.accounts;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import com.redhood.issuetracker.repository.account.accounts.repository.AccountsRepository;
import com.redhood.issuetracker.repository.account.groups.entity.Groups;
import com.redhood.issuetracker.repository.account.groups.repository.GroupsRepository;
import com.redhood.issuetracker.security.SecurityUtils;
import com.redhood.issuetracker.service.account.dto.AccountsDTO;
import com.redhood.issuetracker.service.account.exceptions.EmailAlreadyUsedException;
import com.redhood.issuetracker.service.account.exceptions.InvalidPasswordException;
import com.redhood.issuetracker.service.account.exceptions.UsernameAlreadyUsedException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountsService implements UserDetailsService {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private final Logger log = LoggerFactory.getLogger(AccountsService.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;
    private final Accounts accounts;
    private final GroupsRepository groups;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    public AccountsService(AccountsRepository accountsRepository, PasswordEncoder passwordEncoder, Accounts accounts, GroupsRepository groups) {
        this.accountsRepository = accountsRepository;
        this.passwordEncoder = passwordEncoder;
        this.accounts = accounts;
        this.groups = groups;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Activate user with registration key.
     * @param key registration key
     * @return the activated user. User parameters: {@code activated} set {@code true}, and {@code activationkey} set given {@code key} from param.
     */
    public Optional<Accounts> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return accountsRepository.findOneByActivationKey(key)
                .map( user -> {
                    user.setActivated(true);
                    user.setActivationKey(key);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    /**
     * Reset user password
     * @param newPassword new user password,
     * @param key reset password key
     * @return the user with changed password. User parameters: {@code password} set encoded {@code newPassword}.
     */
    public Optional<Accounts> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return accountsRepository.findOneByResetKey(key)
                .filter( user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    return user;
                });
    }
    /**
     * Request for reset user password.
     * @param email user email address.
     * @return the user with the ability to change password.
     * User parameters: {@code resetKey} seted by {@code generateRandomKey()}, and {@code resetDate} set current time.
     */
    public Optional<Accounts> requestPasswordReset(String email) {
        return accountsRepository.findOneByEmailIgnoreCase(email)
                .filter(Accounts::getActivated)
                .map(user -> {
                    user.setResetKey(generateRandomKey());
                    user.setResetDate(Instant.now());
                    return user;
                });
    }

    /**
     * Method firts of all find existing user by {@code login} or {@code password}.
     * Then create new user with user details included in {@link AccountsDTO} model.
     * To use new account, must be acvitated first.
     * @param accountDTO user details,
     * @param password user password.
     * @return the new user account.
     *
     */
    public Accounts registerAccount(AccountsDTO accountDTO, String password) {
        accountsRepository.findOneByLogin(accountDTO.getLogin().toLowerCase())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                });
        accountsRepository.findOneByEmailIgnoreCase(accountDTO.getEmail())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                });
        Accounts newUser = new Accounts();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(accountDTO.getLogin().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setName(accountDTO.getName());
        newUser.setLastname(accountDTO.getLastname());
        if (accountDTO.getEmail() != null) {
            newUser.setEmail(accountDTO.getEmail().toLowerCase());
        }
        newUser.setActivated(false);
        newUser.setActivationKey(generateRandomKey());
        accountsRepository.save(newUser);
        log.debug("Created User: {}", newUser);
        return newUser;
    }

    /**
     * Remove existing user, which is not activated.
     * @param existingUser existing user,
     * @return the new user account.
     *
     */
    private boolean removeNonActivatedUser(Accounts existingUser) {
        if (existingUser.getActivated()) {
            return false;
        } else {
            accountsRepository.delete(existingUser);
            accountsRepository.flush();
            log.debug("Removed non-activated User: {}", existingUser);
            return true;
        }
    }

    /**
     * Create new user account, without checking if the user exists.
     * New account is activated, and user password is generated randomly.
     * @param accountDTO user details,
     * @return the new user account.
     *
     */
    public Accounts createAccount(AccountsDTO accountDTO) {
        Accounts accounts = new Accounts();
        accounts.setLogin(accountDTO.getLogin().toLowerCase());
        accounts.setName(accountDTO.getName());
        accounts.setLastname(accountDTO.getLastname());
        if (accountDTO.getEmail() != null) {
            accounts.setEmail(accountDTO.getEmail().toLowerCase());
        }
        String encryptedPassword = passwordEncoder.encode(generateRandomKey());
        accounts.setPassword(encryptedPassword);
        accounts.setResetKey(generateRandomKey());
        accounts.setResetDate(Instant.now());
        accounts.setActivated(true);
        accounts.setIdGroup(groups.findOneByDefaults(true).get());
        accounts.setCreatedBy(accountDTO.getCreatedBy().toString());
        accountsRepository.save(accounts);
        log.debug("Created User: {}", accounts);
        return accounts;
    }

    /**
     * Find all users.
     * @return {@code List} of all user {@link Accounts}.
     */
    public List<Accounts> findAll() {
        return accountsRepository.findAllByIdNotNullAndActivatedIsTrue();
    }

    /**
     * Find user by {@code id}.
     * @param id user id.
     * @return {@link Accounts} - single account.
     */
    public Accounts findById(int id) {
        Optional<Accounts> result = accountsRepository.findById(id);
        Accounts account = null;
        if (result.isPresent()) { account = result.get(); }
        return account;
    }

    /**
     * Find user by {@code login}.
     * @param login user login.
     * @return {@link Accounts} - single account.
     */
    public Accounts findByLogin(String login) {
        Optional<Accounts> result = accountsRepository.findOneByLogin(login);
        Accounts account = null;
        if (result.isPresent()) { account = result.get(); }
        return account;
    }

    /**
     * Update user by given details in {@link AccountsDTO} model.
     * @param accountsDTO user details.
     * @return {@link Optional} (if given user exists) updated account.
     */
    public Optional<AccountsDTO> updateAccount(AccountsDTO accountsDTO) {
        return Optional
                .of(accountsRepository.findById(accountsDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setLogin(accountsDTO.getLogin().toLowerCase());
                    if (accountsDTO.getEmail() != null) {
                        user.setEmail(accountsDTO.getEmail().toLowerCase());
                    }
                    user.setActivated(accountsDTO.getActivated());
                    user.setBlocked(accountsDTO.isBlocked() == true ? Instant.now() : null);
                    user.setName(accountsDTO.getName());
                    user.setLastname(accountsDTO.getLastname());
                    user.setIdGroup(accountsDTO.getIdGroup());
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }).map(AccountsDTO::new);
    }

    public void updateUser(String firstName, String lastName, String email, String login) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(accountsRepository::findOneByLogin)
                .ifPresent(user -> {
                    user.setName(firstName);
                    user.setLastname(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLogin(login);
                    log.debug("Changed Information for User: {}", user);
                });
    }

    /**
     * Delete user by {@code id}.
     * @param id user id.
     */
    public void deleteById(int id) {
        accountsRepository.findById(id)
                .ifPresent( user -> {
                    accountsRepository.delete(user);
                    log.debug("Deleted User: {}", user);
                });
    }

    /**
     * Delete user by {@code login}.
     * @param login user login.
     */
    public void deleteByLogin(String login) {
        accountsRepository.findOneByLogin(login)
                .ifPresent(user -> {
                    accountsRepository.delete(user);
                    log.debug("Deleted user: {}", user);
                });
    }

    /**
     * Changing current login user password. User can change own password by entering old and then new password.
     * @param currentClearTextPassword plain text password
     * @param newPassword new password
     */
    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(accountsRepository::findOneByLogin)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    log.debug("Changed password for User: {}", user);
                });
    }

    /**
     * Method generate random key.
     * @return {@link String} - randomly generated key.
     */
    public static String generateRandomKey() {
        return RandomStringUtils.random(20, 0, 0, true, true, (char[])null, SECURE_RANDOM);
    }
    //------------------------------------------------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public Optional<Accounts> getUserWithAuthoritiesByLogin(String login) {
        return accountsRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<Accounts> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(accountsRepository::findOneByLogin);
    }
    //------------------------------------------------------------------------------------------------------------------
    // UserDetailsService
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Find account with given username (login).
     * @param username - username or login.
     * @return {@link UserDetails} - if user exists, return account extended by {@link UserDetails} interface.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountsRepository.findOneByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Could not find user!"));
    }
    //------------------------------------------------------------------------------------------------------------------
}
