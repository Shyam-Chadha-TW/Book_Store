package com.example.BookStore.BookStore.Advices;

import com.example.BookStore.BookStore.Exception.ResourceAlreadyExist;
import com.example.BookStore.BookStore.Exception.ResourceNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFound.class)
    private ResponseEntity<APIError> handleResourceNotFound(ResourceNotFound e){
        APIError apiError = APIError.builder()
                .status(HttpStatus.NOT_FOUND)
                .errMsg(e.getMessage())
                .build();
        return new ResponseEntity<>(apiError,apiError.getStatus());
    }
    @ExceptionHandler(ResourceAlreadyExist.class)
    private ResponseEntity<APIError> handleResourceAlreadyExist(ResourceAlreadyExist e){
        APIError apiError = APIError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errMsg(e.getMessage())
                .build();
        return new ResponseEntity<>(apiError,apiError.getStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<APIError> handleMethodArgsNotValid(MethodArgumentNotValidException e){
        APIError apiError = APIError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errMsg(e.getMessage())
                .build();
        return new ResponseEntity<>(apiError,apiError.getStatus());
    }
    @ExceptionHandler(Exception.class)
    private ResponseEntity<APIError> handleAllOtherExceptions(Exception e){
        APIError apiError = APIError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .errMsg(e.getMessage())
                .build();
        return new ResponseEntity<>(apiError,apiError.getStatus());
    }
}
