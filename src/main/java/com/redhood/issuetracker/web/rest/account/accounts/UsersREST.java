package com.redhood.issuetracker.web.rest.account.accounts;

import com.redhood.issuetracker.repository.account.accounts.entity.Accounts;
import com.redhood.issuetracker.service.account.accounts.AccountsService;
import com.redhood.issuetracker.service.account.dto.AccountsDTO;
import com.redhood.issuetracker.web.utils.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public UsersREST(AccountsService accountsService) {
        this.accountsService = accountsService;
    }
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - GET
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - POST
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - PUT
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------
    @PutMapping("/account/accounts/")
    public ResponseEntity updateuser(@Valid @RequestBody AccountsDTO accountsDTO) {
        Optional<Accounts> existingUser = accountsRepository.findOneByEmailIgnoreCase(accountsDTO.getEmail());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(accountsDTO.getId()))) {
//            return ResponseHandler.generateResponse(null, "User email cannot be the same!", HttpStatus.BAD_REQUEST, accountsDTO);
//        }
//        existingUser = accountsRepository.findOneByLogin(accountsDTO.getLogin().toLowerCase());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(accountsDTO.getId()))) {
//            return ResponseHandler.generateResponse(null, "User login cannot be the same!", HttpStatus.BAD_REQUEST, accountsDTO);
//        }
        Optional<AccountsDTO> updateUser = accountsService.updateAccount(accountsDTO);
        return ResponseHandler.generateResponse(HttpStatus.OK, updateUser);
    }

    //------------------------------------------------------------------------------------------------------------------
    // REST Controller - DELETE
    //------------------------------------------------------------------------------------------------------------------
    //
    //------------------------------------------------------------------------------------------------------------------
}
