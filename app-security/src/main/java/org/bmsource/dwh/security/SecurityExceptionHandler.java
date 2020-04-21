package org.bmsource.dwh.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SecurityExceptionHandler.class);

    @ExceptionHandler({ SecurityException.class, AccessDeniedException.class })
    @ResponseBody
    public ResponseEntity<?> handleSecurityException(Exception e) {
        logger.trace(e.getMessage(), e);
        return errorHttpResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public ResponseEntity<?> handleAnyException(Exception e) {
        logger.error(e.getMessage(), e);
        return errorHttpResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<ExceptionMessage> errorHttpResponse(Throwable throwable, HttpStatus status) {
        return new ResponseEntity<>(new ExceptionMessage(throwable), status);
    }
}
