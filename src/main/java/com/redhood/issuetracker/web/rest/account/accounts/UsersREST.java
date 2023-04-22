package com.redhood.issuetracker.web.rest.account.accounts;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import com.redhood.issuetracker.repository.account.accounts.repository.AccountsRepository;
import com.redhood.issuetracker.security.SecurityUtils;
import com.redhood.issuetracker.service.account.accounts.AccountsService;
import com.redhood.issuetracker.service.account.dto.AccountsDTO;
import com.redhood.issuetracker.service.account.dto.PasswordChangeDTO;
import com.redhood.issuetracker.service.account.exceptions.EmailAlreadyUsedException;
import com.redhood.issuetracker.web.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api/", produces = "application/json")

public class UsersREST {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    private final Logger log = LoggerFactory.getLogger(UsersREST.class);
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------
    private AccountsService accountsService;

    private AccountsRepository accountsRepository;

    @Autowired
    public UsersREST(AccountsService accountsService, AccountsRepository accountsRepository) {
        this.accountsService = accountsService;
        this.accountsRepository = accountsRepository;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - GET
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@code GET /api/account/accounts} : get current login user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body containing data (user) and transaction status.
     */
    @GetMapping("/account/accounts")
    public ResponseEntity getUser() {
        Accounts currentUser = accountsService.getUserWithAuthorities()
                .orElseThrow(() -> new UsernameNotFoundException("User could not be found!"));
        return ResponseHandler.generateResponse(HttpStatus.OK, currentUser);
    }

    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - POST
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------
    /**
     * {@code GET /api/account/accounts} : update current login user data.
     * @param accountsDTO user data: login, email, name, last name.
     */
    @PostMapping("/account/accounts/")
    public void saveUser(@Valid @RequestBody AccountsDTO accountsDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new UsernameNotFoundException("Current user login not be found!"));
        Optional<Accounts> existingUser = accountsRepository.findOneByEmailIgnoreCase(accountsDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equals(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<Accounts> user = accountsRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User could not be found!");
        }
        accountsService.updateUser(
                accountsDTO.getName(),
                accountsDTO.getLastname(),
                accountsDTO.getEmail(),
                accountsDTO.getLogin());
    }

    @PostMapping(path = "account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        accountsService.changePassword(passwordChangeDTO.getCurrentPassword(), passwordChangeDTO.getNewPassword());
    }
    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - PUT
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping("/account/accounts/")
    public ResponseEntity updateUser(@Valid @RequestBody AccountsDTO accountsDTO) {
        Optional<AccountsDTO> updateUser = accountsService.updateAccount(accountsDTO);
        return ResponseHandler.generateResponse(HttpStatus.OK, updateUser);
    }

    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - DELETE
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------
}
