package com.example.demo.exception.UserException;


import com.example.demo.exception.Message;
import com.example.demo.exception.base.BaseException;

public class UserAlreadyExistException extends BaseException {
    public UserAlreadyExistException() {

    }

    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(Message msg, Object... params) {
        super(msg, params);
    }
}
