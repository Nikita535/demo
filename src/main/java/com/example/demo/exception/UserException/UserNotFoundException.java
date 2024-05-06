package com.example.demo.exception.UserException;


import com.example.demo.exception.Message;
import com.example.demo.exception.base.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {

    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Message msg, Object... params) {
        super(msg, params);
    }
}
