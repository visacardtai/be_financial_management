package com.trainingfinance.apisystem.exception;
import java.text.MessageFormat;
public class NotFoundException extends RuntimeException {
    public NotFoundException(final Long id){
        super(MessageFormat.format("Could not find item with id: {0}", id));
    }
}
