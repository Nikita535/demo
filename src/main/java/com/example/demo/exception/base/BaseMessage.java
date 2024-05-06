package com.example.demo.exception.base;


import com.example.demo.exception.Message;

public enum BaseMessage implements Message {
    SUCCESS(0, "message.success"),
    UNKNOWN_SERVER_ERROR(-1, "error.unknownServerError"),
    INVALID_CREDENTIALS(-2, "error.invalidCredentials"),
    ENTITY_NOT_FOUND(-3, "error.entityNotFound"),
    BAD_REQUEST(-4, "error.badRequest"),
    UNAUTHORIZED(-5, "error.unauthorized"),
    ACCESS_DENIED(-6, "error.accessDenied"),
    ACCOUNT_DISABLED(-7, "error.accountDisabled"),
    ENTITY_CODE_EXISTS(-8, "error.entityCodeExists"),
    ENTITY_CODE_NOT_FOUND(-9, "error.entityCodeNotFound"),
    ENTITY_CONSTRAINT_VIOLATION(-10, "error.constraintViolation");

    private final int code;
    private final String text;

    private BaseMessage(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }

    public static Message valueOf(int code) {
        BaseMessage[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Message message = var1[var3];
            if (message.getCode() == code) {
                return message;
            }
        }

        return null;
    }
}
