package com.jalon.exceptin.response;

public class ApiResponse {
    public String code;
    public String msg;
    public Object data;

    public ApiResponse(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResponse(String code, String msg) {
        this(code, msg, null);
    }


}
