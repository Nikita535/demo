package com.example.demo.exception.TeamException;


import com.example.demo.exception.Message;
import com.example.demo.exception.base.BaseException;

public class TeamNotFoundException extends BaseException {
    public TeamNotFoundException() {

    }

    public TeamNotFoundException(Throwable cause) {
        super(cause);
    }

    public TeamNotFoundException(String message) {
        super(message);
    }

    public TeamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamNotFoundException(Message msg, Object... params) {
        super(String.format(msg.getText(), params[0]));
    }
}
