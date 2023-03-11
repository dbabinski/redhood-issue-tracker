package com.redhood.issuetracker.web.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    //------------------------------------------------------------------------------------------------------------------
    // Fields
    //------------------------------------------------------------------------------------------------------------------
    public static final String HTTPSTATUS_OK = "Success!";
    public static final String HTTPSTATUS_NOT_FOUND = "Not found!";
    public static final String HTTPSTATUS_BAD_REQUEST = "Bad request!";
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    // Methods
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Supporting methods to provide response handler.
     */
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
        return generateResponse(null, message, status, null);
    }

    public static ResponseEntity<Object> generateResponse(HttpStatus status) {
        return generateResponse(null, null, status, null);
    }

    public static ResponseEntity<Object> generateResponse(HttpStatus status, Object responseObj) {
        return generateResponse(null, null, status, responseObj);
    }

    public static ResponseEntity<Object> generateResponse(HttpHeaders httpHeaders,  HttpStatus status, Object responseObj) {
        return generateResponse(httpHeaders,null, status, responseObj);
    }

    /**
     * Main method to provide response handler. Depending on chosen method,
     * return {@link ResponseEntity} - JSON object with {@code message"}, {@code status} and {@code data}.
     * Also set up http headers and response status from {@link HttpStatus} enum.
     * @param httpHeaders
     * @param message custom message
     * @param status {@link HttpStatus} of request
     * @param responseObj object containing data
     * @return {@link ResponseEntity} with JSON object with {@code message"}, {@code status} and {@code data}.
     */
    public static ResponseEntity<Object> generateResponse(HttpHeaders httpHeaders, String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (status.equals(HttpStatus.OK)) {
            map.put("message", message == null ? HTTPSTATUS_OK : message);
        } else if (status.equals(HttpStatus.NOT_FOUND)) {
            map.put("message", message == null ? HTTPSTATUS_NOT_FOUND : message);
        } else if (status.equals(HttpStatus.BAD_REQUEST)) {
                map.put("message", message == null ? HTTPSTATUS_BAD_REQUEST : message);
        } else {
            map.put("message", message);
        }

        map.put("status", status.value());

        if (responseObj == null || responseObj.equals(null)) {
            map.put("data", null);
        } else {
            map.put("data", responseObj);
        }
        return new ResponseEntity<Object>(map, httpHeaders,status);
    }

    /**
     * Method return response whem JWT was created. When JWT token is added to cookies, additionaly generate response
     * with {@code JWT} and {@code customClaims}, to handle user privileges.
     * @param httpHeaders
     * @param message
     * @param status
     * @param responseObj
     * @param customClaims
     * @return {@link ResponseEntity} with JSON object with {@code message"}, {@code status} and {@code data}.
     */
    public static ResponseEntity<Object> generateResponseJWT(HttpHeaders httpHeaders, String message, HttpStatus status, Object responseObj, Object customClaims) {
        Map<String, Object> map = new HashMap<String, Object>();

        //TODO: generateResponseJWT
        return new ResponseEntity<Object>(map, httpHeaders, status);
    }
    //------------------------------------------------------------------------------------------------------------------
}
