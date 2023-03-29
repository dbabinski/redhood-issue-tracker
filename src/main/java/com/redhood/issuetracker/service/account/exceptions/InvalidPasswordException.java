package com.redhood.issuetracker.service.account.exceptions;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException() {
        super("Password is incorrect!");
    }
}
