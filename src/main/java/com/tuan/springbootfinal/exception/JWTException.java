package com.tuan.springbootfinal.exception;

import io.jsonwebtoken.JwtException;

public class JWTException extends JwtException {
    public JWTException(String message) {
        super(message);
    }
}
