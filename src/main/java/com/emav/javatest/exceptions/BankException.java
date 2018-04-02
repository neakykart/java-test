package com.emav.javatest.exceptions;

/**
 * Created by Elinam on 02/04/2018.
 */
public class BankException extends Exception {
    private BankErrorCode errorCode;

    public BankException(String message, BankErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BankErrorCode getErrorCode() {
        return errorCode;
    }
}
