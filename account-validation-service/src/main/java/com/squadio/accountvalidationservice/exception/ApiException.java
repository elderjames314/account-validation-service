package com.squadio.accountvalidationservice.exception;

/**
 * @author jamesoladimeji
 * @created 29/08/2021 - 4:20 PM
 * @project IntelliJ IDEA
 */
public class ApiException extends RuntimeException{
    public  ApiException(String message) {
        super(message);
    }
}
