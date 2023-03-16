package com.redhood.issuetracker.service.account.exceptions;

public class UsernameAlreadyUsedException extends RuntimeException{
    public UsernameAlreadyUsedException() {
        super("Login name already used!");
    }
}
