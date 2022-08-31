package com.beanfood.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends InternalException {


    private static final String MESSAGE = "유효하지 않은 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
