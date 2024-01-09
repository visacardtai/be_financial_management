package com.trainingfinance.apisystem.exception;
import java.text.MessageFormat;

public class TokenExpirationException extends RuntimeException {
    public TokenExpirationException(String m){
        super(m);
    }

}
