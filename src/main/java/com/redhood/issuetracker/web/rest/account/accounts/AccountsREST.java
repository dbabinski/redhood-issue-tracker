package com.redhood.issuetracker.web.rest.account.accounts;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import com.redhood.issuetracker.repository.account.accounts.repository.AccountsRepository;
import com.redhood.issuetracker.service.account.accounts.AccountsService;
import com.redhood.issuetracker.service.account.dto.AccountsDTO;
import com.redhood.issuetracker.web.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/admin", produces="application/json")
public class AccountsREST {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private final Logger log = LoggerFactory.getLogger(AccountsREST.class);
    private AccountsService accountsService;
    private final AccountsRepository accountsRepository;
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------------------------------
    @Autowired
    public AccountsREST(AccountsService accountsService, AccountsRepository accountsRepository) {
        this.accountsService = accountsService;
        this.accountsRepository = accountsRepository;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - GET
    //------------------------------------------------------------------------------------------------------------------
    /**
     * {@code GET /api/admin/account/accounts} : get all users with all the details - calling this are only allowed for the administrators.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing data (all users), message and transaction status.
     */
    @GetMapping("/account/accounts")
    public ResponseEntity getAllAccounts() {
        final List<Accounts> accounts = accountsService.findAll();
        if (accounts.equals(null) || accounts == null) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND);
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, accounts);
        }
    }

    /**
     * {@code GET /api/admin/account/accounts/{login}} : get user with specific "login".
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body containing data (user), message and transaction status.
     */
    @GetMapping("/account/accounts/{login}")
    public ResponseEntity getAccountByLogin(@PathVariable @Pattern(regexp = Accounts.LOGIN_REGEX) String login) {
        final Accounts account = accountsService.findByLogin(login);
        if (account.equals(null) || account == null) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.generateResponse(HttpStatus.OK, account);
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - POST
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@code POST /api/admin/acocunt/accounts/} : Create new user.
     * @param accountsDTO user data
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body containing data (new user), message and transaction status.
     */
    @PostMapping("/account/accounts")
    public ResponseEntity getAccount(@Valid @RequestBody AccountsDTO accountsDTO) {
        if (accountsDTO.getId() != null) {
            return ResponseHandler.generateResponse(null, "User cannot already have an ID!", HttpStatus.BAD_REQUEST, accountsDTO);
        } else if (accountsRepository.findOneByLogin(accountsDTO.getLogin().toLowerCase()).isPresent()) {
            return ResponseHandler.generateResponse(null, "User login exists!", HttpStatus.BAD_REQUEST, accountsDTO);
        } else if (accountsRepository.findOneByEmailIgnoreCase(accountsDTO.getEmail()).isPresent()) {
            return ResponseHandler.generateResponse(null, "User email exists!!", HttpStatus.BAD_REQUEST, accountsDTO);
        } else {
            Accounts newUser = accountsService.createAccount(accountsDTO);
            return ResponseHandler.generateResponse(HttpStatus.OK, newUser);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - PUT
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@code PUT /api/admin/acocunt/accounts/} : Update user data.
     * @param accountsDTO user details for change.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body containing data (new user), message and transaction status.
     */
    @PutMapping("/account/accounts/")
    public ResponseEntity updateAccount(@Valid @RequestBody AccountsDTO accountsDTO) {
        Optional<AccountsDTO> updateUser = accountsService.updateAccount(accountsDTO);
        return ResponseHandler.generateResponse(HttpStatus.OK, updateUser);
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - DELETE
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Delete user by login.
     * @param login user login
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body containing deleted (user), message and transaction status.
     */
    @DeleteMapping("/account/accounts/{login}")
    public ResponseEntity deleteAccount(@PathVariable @Pattern(regexp = Accounts.LOGIN_REGEX) String login) {
        Accounts account = accountsService.findByLogin(login);
        if (account == null) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, login);
        } else {
            accountsService.deleteByLogin(login);
            log.debug("User login: {} deleted", login);
            return ResponseHandler.generateResponse(HttpStatus.OK, account);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}
