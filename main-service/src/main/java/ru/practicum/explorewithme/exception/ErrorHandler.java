package ru.practicum.explorewithme.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestValidationException(Exception e) {
        return new ErrorResponse("Ошибка валидации данных запроса", e.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestParamException(Exception e) {
        return new ErrorResponse("Ошибка валидации параметров запроса", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserIdException(UserIdException e) {
        return new ErrorResponse("Ошибка id пользователя", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEventIdException(EventIdException e) {
        return new ErrorResponse("Ошибка id события", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryIdException(CategoryIdException e) {
        return new ErrorResponse("Ошибка id категории", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRequestIdException(RequestIdException e) {
        return new ErrorResponse("Ошибка id запроса", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse("Ошибка поиска данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCompilationIdException(CompilationIdException e) {
        return new ErrorResponse("Ошибка id подборки", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEventTimeException(InvalidEventTimeException e) {
        return new ErrorResponse("Ошибка времени события", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInitiatorIdException(InitiatorIdException e) {
        return new ErrorResponse("Ошибка валидации id инициатора", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUpdatePublishedEventException(UpdatePublishedEventException e) {
        return new ErrorResponse("Ошибка обновления события", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        return new ErrorResponse("Ошибка ввода данных", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(HttpServerErrorException.InternalServerError e) {
        return new ErrorResponse("Ошибка на стороне сервера", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInvalidRequestDataException(InvalidRequestDataException e) {
        return new ErrorResponse("Ошибка вводных данных", e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationAndRequestParamsErrors(Throwable e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
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
