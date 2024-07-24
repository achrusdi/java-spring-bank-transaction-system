package com.ilu.spring_starter.apis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> extends ResponseEntity<ApiResponse.Body<T>> {

    public ApiResponse(HttpStatus status, String message, T data) {
        super(new Body<>(status.value(), message, data), status);
    }

    @AllArgsConstructor
    @Data
    public static class Body<T> {
        private int code;
        private String message;
        private T data;
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK, message, data);
    }

    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(HttpStatus.OK, message, null);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(HttpStatus.CREATED, message, data);
    }

    public static ApiResponse<Void> noContent(String message) {
        return new ApiResponse<>(HttpStatus.NO_CONTENT, message, null);
    }

    // public static ApiResponse<String> badRequest(String message) {
    //     return new ApiResponse<>(HttpStatus.BAD_REQUEST, message, null);
    // }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, message, null);
    }

    public static ApiResponse<String> notFound(String message) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND, message, null);
    }

    public static ApiResponse<String> internalServerError(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }

    public static <T> ApiResponse<T> error(String error) {
        throw new UnsupportedOperationException(error);
    }
}