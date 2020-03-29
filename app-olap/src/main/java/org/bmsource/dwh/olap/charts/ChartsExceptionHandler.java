package org.bmsource.dwh.olap.charts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChartsExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ChartsExceptionHandler.class);

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public ResponseEntity<?> handleAnyException(Exception e) {
        return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ResponseEntity<ExceptionMessage> errorResponse(Throwable throwable,
                                                             HttpStatus status) {
        log.error("error caught: " + throwable.getMessage(), throwable);
        return response(new ExceptionMessage(throwable), status);
    }

    protected <T> ResponseEntity<T> response(T body, HttpStatus status) {
        log.debug("Responding with a status of {}", status);
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }
}
