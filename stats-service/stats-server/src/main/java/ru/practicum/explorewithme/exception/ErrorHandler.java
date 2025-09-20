package ru.practicum.explorewithme.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDateException(InvalidDateException e) {
        return new ErrorResponse("Ошибка введённой даты", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(HttpServerErrorException.InternalServerError e) {
        return new ErrorResponse("Ошибка на стороне сервера", e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse handleAnyException(Exception e) {
        return new ErrorResponse("Неучтённая ошибка", e.getMessage());
    }

    @Getter
    public static class ErrorResponse {
        private final String error;
        private final String description;

        private ErrorResponse(String error, String description) {
            this.error = error;
            this.description = description;
        }
    }
}
