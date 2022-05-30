package com.oldaim.fkbackend.exceptionhandler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import javax.validation.ValidationException;
import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class , NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException e) {
        e.printStackTrace();
        ErrorResponse response = createErrorResponse(e, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleSQLErrorException(RuntimeException e) {
        e.printStackTrace();
        ErrorResponse response = createErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(RuntimeException e) {
        e.printStackTrace();
        ErrorResponse response = createErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JwtValidationException.class})
    public ResponseEntity<ErrorResponse> handleJwtValidationException(JwtValidationException e) {
        ErrorResponse response = createErrorResponse(e, e.getStatus());
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler({MessagingException.class, GeneralSecurityException.class})
    public ResponseEntity<ErrorResponse> handleGmailValidationException(RuntimeException e){
        e.printStackTrace();
        ErrorResponse response = createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>( response , HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private ErrorResponse createErrorResponse(RuntimeException e, HttpStatus httpStatus) {

        String errMsg = null;

        if (e instanceof ValidationException) {
            errMsg = e.getMessage().split("\\{")[1].split("'")[1];
            log.info(e.getMessage());
        }
        else {
            errMsg = e.getMessage();
        }
        return new ErrorResponse(httpStatus, errMsg);
    }

    private ErrorResponse createErrorResponse(Errors e, HttpStatus httpStatus) {

        String errMsg = null;

        if (e instanceof MethodArgumentNotValidException) {
            errMsg = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();
        } else {
            errMsg = e.getAllErrors().toString();
        }
        return new ErrorResponse(httpStatus, errMsg);
    }
}
