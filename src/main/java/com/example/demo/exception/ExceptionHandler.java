package com.example.demo.exception;

import diplom.eventservice.exception.EventException.EventNotFoundException;
import diplom.eventservice.exception.EventException.EventServiceMessages;
import diplom.eventservice.exception.TeamException.TeamNotFoundException;
import diplom.eventservice.exception.TeamException.TeamServiceMessages;
import diplom.eventservice.exception.UserException.UserAlreadyExistException;
import diplom.eventservice.exception.UserException.UserNotFoundException;
import diplom.eventservice.exception.UserException.UserServiceMessages;
import diplom.eventservice.exception.base.BaseException;
import diplom.eventservice.model.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageHelper messageHelper;
    @Value("${app.rest.response.exception-stacktrace:false}")
    protected boolean returnStacktrace;
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler({
            UserAlreadyExistException.class,
            UserNotFoundException.class,
            TeamNotFoundException.class,
            EventNotFoundException.class})
    private ResponseEntity<Object> handleUserException(BaseException ex){
        if(ex instanceof UserAlreadyExistException){
            return buildErrorResult(UserServiceMessages.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST,ex);
        }else if(ex instanceof UserNotFoundException){
            return buildErrorResult(UserServiceMessages.USER_NOT_FOUND, HttpStatus.BAD_REQUEST,ex);
        }else if(ex instanceof TeamNotFoundException){
            return buildErrorResult(TeamServiceMessages.TEAM_NOT_FOUND, HttpStatus.BAD_REQUEST,ex);
        }else if(ex instanceof EventNotFoundException){
            return buildErrorResult(EventServiceMessages.EVENT_NOT_FOUND, HttpStatus.BAD_REQUEST,ex);
        }
        return buildErrorResult(UserServiceMessages.USER_NOT_FOUND, HttpStatus.BAD_REQUEST,ex);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();

        List<String> orderedErrors = new ArrayList<>();
        List<String> fieldOrder = getFieldOrder();

        for (String fieldName : fieldOrder) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                if (error.getField().equals(fieldName)) {
                    orderedErrors.add(String.format("Field '%s': %s", error.getField(), error.getDefaultMessage()));
                }
            }
        }

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("errors", orderedErrors);

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    private List<String> getFieldOrder() {
        List<String> fieldOrder = new ArrayList<>();
        fieldOrder.add("username");
        fieldOrder.add("password");
        fieldOrder.add("email");
        return fieldOrder;
    }
    protected ResponseEntity<Object> buildErrorResult(Message message, HttpStatus status, BaseException ex, Object... params) {
        if (ex != null) {
            log.error(ex.getMessage(), ex);
        }

        if (message == null) {
            return new ResponseEntity(Result.error(-1, (String) Optional.ofNullable(ex).map(Throwable::getMessage).orElse(null), this.returnStacktrace ? ex : null), status);
        } else {
            String text = ex.getParams() != null && ex.getParams().length != 0 ? String.format(this.messageHelper.getMessageWithParams(message.getText(), params),ex.getParams()[0]) : this.messageHelper.getMessage(message.getText());
            return new ResponseEntity(Result.error(message.getCode(), text, this.returnStacktrace ? ex : null), status);
        }
    }

    protected MessageHelper getMessageHelper() {
        return this.messageHelper;
    }
}
