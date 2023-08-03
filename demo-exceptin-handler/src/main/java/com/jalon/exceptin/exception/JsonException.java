package com.jalon.exceptin.exception;

public class JsonException extends RuntimeException{
    private String code;
    private String errorMsg;
    public JsonException(String code, String errorMsg) {
        super(errorMsg);
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
