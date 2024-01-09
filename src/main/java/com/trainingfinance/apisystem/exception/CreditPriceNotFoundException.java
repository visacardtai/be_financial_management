package com.trainingfinance.apisystem.exception;

import java.text.MessageFormat;

public class CreditPriceNotFoundException extends RuntimeException{
    public CreditPriceNotFoundException(Long id) {
        super(MessageFormat.format("Could not find Credit-Price with id: {0}", id));
    }
}
