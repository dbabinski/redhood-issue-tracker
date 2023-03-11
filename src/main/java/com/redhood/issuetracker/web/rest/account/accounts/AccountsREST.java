package com.redhood.issuetracker.web.rest.account.accounts;

import com.redhood.issuetracker.service.account.accounts.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/", produces = "application/json")

public class AccountsREST {

    private final Logger log = LoggerFactory.getLogger(AccountsREST.class);

    private AccountsService accountsService;

    @Autowired
    public AccountsREST(AccountsService accountsService) {
        this.accountsService = accountsService;
    }



}
