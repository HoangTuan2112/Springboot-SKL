package com.tuan.springbootfinal.exception;

import com.tuan.springbootfinal.constant.WrapResStatus;
import com.tuan.springbootfinal.response.WrapRes;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("HandleException.handleMethodArgumentNotValid");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                error -> {
                    String field = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(field, message);
                });
        return handleExceptionInternal(ex, WrapRes.error(WrapResStatus.BAD_REQUEST,errors.toString()), headers, status, request);
    }




    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        System.out.println("HandleException.handleServiceException");
        return handleExceptionInternal(ex, WrapRes.error(WrapResStatus.SERVICE_ERROR,ex.getMessage()), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        System.out.println("HandleException.handleNotFoundException");
        return handleExceptionInternal(ex, WrapRes.error(WrapResStatus.NOT_FOUND,ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        System.out.println("HandleException.handleUnauthorizedException");
        return handleExceptionInternal(ex, WrapRes.error(WrapResStatus.UNAUTHORIZED,ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
    //Jwt exception
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
        System.out.println("HandleException.handleJwtException");
        return handleExceptionInternal(ex, WrapRes.error(WrapResStatus.SECURITY_ERROR,ex.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN ,request);
    }
}
