package com.example.demo.exception.UserException;

import com.example.demo.exception.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserServiceMessages implements Message {

    USER_ALREADY_EXISTS(-10,"error.user_already_exists"),
    USER_NOT_FOUND(-11,"error.user_not_found");

    private final int code;
    private final String text;
}
