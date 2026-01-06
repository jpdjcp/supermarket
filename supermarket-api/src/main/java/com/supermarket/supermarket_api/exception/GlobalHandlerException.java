package com.supermarket.supermarket_api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler({
            SaleNotFoundException.class,
            BranchNotFoundException.class,
            ProductNotFoundException.class
    })
    public ErrorResponse handleNotFound(RuntimeException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
