package com.redhood.issuetracker.service.account.permissions.handler;

public class PermissionsException extends Exception{
    public static final String FORBIDDEN = "No access!";

    public PermissionsException() {
        super(FORBIDDEN);
    }

    public PermissionsException(String message){
        super(message);
    }
}
