package com.beanfood.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class InternalException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    abstract public int getStatusCode();

}
