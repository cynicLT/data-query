package org.cynic.data_query.controller.advice;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.cynic.data_query.domain.ApplicationException;
import org.cynic.data_query.domain.http.ErrorHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalErrorAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorAdvice.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationException.class)
    public final ErrorHttp handleApplicationException(ApplicationException exception) {
        return new ErrorHttp(
            exception.getCode(),
            exception.getValues()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public final ErrorHttp handleMissingParameterException(MissingServletRequestParameterException exception) {
        return new ErrorHttp(
            "error.parameter.missing",
            Map.entry("name", exception.getParameterName()),
            Map.entry("type", exception.getParameterType())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ErrorHttp handleTypeException(MethodArgumentTypeMismatchException exception) {
        return new ErrorHttp(
            "error.parameter.invalid-type",
            Map.entry("name", exception.getName()),
            Map.entry("value", Objects.toString(exception.getValue(), StringUtils.EMPTY)),
            Map.entry("type", ClassUtils.getSimpleName(exception.getRequiredType()))
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentConversionNotSupportedException.class)
    public final ErrorHttp handleArgumentConversionException(MethodArgumentConversionNotSupportedException exception) {
        return new ErrorHttp(
            "error.parameter.non-convertable",
            Map.entry("name", exception.getName()),
            Map.entry("value", Objects.toString(exception.getValue(), StringUtils.EMPTY)),
            Map.entry("type", ClassUtils.getSimpleName(exception.getRequiredType()))
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ErrorHttp handleHttpMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return new ErrorHttp(
            "error.http-method.not-supported",
            Map.entry("method", exception.getMethod())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorHttp handleConstraintViolationException(ConstraintViolationException exception) {
        return new ErrorHttp(
            "error.validation.parameter",
            exception.getConstraintViolations().stream()
                .map(it ->
                    Map.entry(
                        StreamSupport.stream(it.getPropertyPath().spliterator(), false)
                            .reduce((a, b) -> b)
                            .map(Path.Node::getName)
                            .orElse(StringUtils.EMPTY),
                        Map.of(
                            "message", it.getMessage(),
                            "value", it.getInvalidValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorHttp handleBindException(BindException exception) {
        return new ErrorHttp(
            "error.validation.body",
            exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(it -> Objects.nonNull(it.getDefaultMessage()))
                .map(it -> Map.entry(it.getField(), it.getDefaultMessage()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ErrorHttp handleDataAccessException(DataAccessException exception) {
        LOGGER.error("", exception);

        return new ErrorHttp("error.database",
            Map.entry("message", ExceptionUtils.getRootCauseMessage(exception)),
            Map.entry("stack", ExceptionUtils.getStackTrace(exception))
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public final ErrorHttp handleUnknownException(Throwable exception) {
        LOGGER.error("", exception);

        return new ErrorHttp(
            "error.unknown",
            Map.entry("message", ExceptionUtils.getRootCauseMessage(exception)),
            Map.entry("stack", ExceptionUtils.getStackTrace(exception))
        );
    }
}
