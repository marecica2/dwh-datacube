package org.bmsource.dwh.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SecurityExceptionHandler.class);

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public ResponseEntity<?> handleAnyException(Exception e) {
        return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ SecurityException.class })
    @ResponseBody
    public ResponseEntity<?> handleSecurityException(Exception e) {
        return errorResponse(e, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ExceptionMessage> errorResponse(Throwable throwable, HttpStatus status) {
        logger.error(throwable.getMessage(), throwable);
        return response(new ExceptionMessage(throwable), status);
    }

    protected <T> ResponseEntity<T> response(T body, HttpStatus status) {
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }
}
