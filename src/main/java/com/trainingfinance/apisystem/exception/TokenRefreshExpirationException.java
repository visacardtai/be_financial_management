package com.trainingfinance.apisystem.exception;

public class TokenRefreshExpirationException extends RuntimeException {
    public TokenRefreshExpirationException(String m){
        super(m);
    }
}
