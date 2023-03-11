package com.redhood.issuetracker.service.account.errors;

public class UsernameAlreadyUsedException extends RuntimeException{
    public UsernameAlreadyUsedException() {
        super("Login name already used!");
    }
}
