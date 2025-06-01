package com.duowngtora.spring_ollama.constant;

import org.springframework.http.HttpStatus;

public class CustomResponse {
    private int code = HttpStatus.OK.value();
    private String message = "Success";
    private boolean status = true;
    private Object data = null;

    public static CustomResponse Success(Object data) {
        return new CustomResponse(data);
    }

    public static CustomResponse Success() {
        return new CustomResponse();
    }

    public static CustomResponse Error(Exception ex) {
        return new CustomResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), false,ex.getStackTrace());
    }

    public CustomResponse() {
    }

    public CustomResponse(Object data) {
        this.data = data;
    }

    public CustomResponse(int code, String message, boolean status, Object data) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
