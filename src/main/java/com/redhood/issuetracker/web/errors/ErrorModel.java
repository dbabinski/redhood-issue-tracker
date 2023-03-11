package com.redhood.issuetracker.web.errors;

import java.io.Serializable;

public class ErrorModel implements Serializable {

    public static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;

    public ErrorModel(String objectName, String field, String message) {
        this.objectName = objectName;
        this.field = field;
        this.message = message;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
