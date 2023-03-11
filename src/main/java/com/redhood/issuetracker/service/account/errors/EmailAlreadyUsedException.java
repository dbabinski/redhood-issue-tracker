package com.redhood.issuetracker.service.account.errors;

public class EmailAlreadyUsedException extends RuntimeException{
    public EmailAlreadyUsedException() {
        super("Email is already in use!");
    }
}
