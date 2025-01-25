package com.example.productservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleConstraintValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(mapMethodArgumentNotValidExceptionMessage(exception.getDetailMessageArguments()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    /* MAP
    from:
    [
        "",
        "role: Role id es requerido, and email: debe ser una dirección de correo electrónico con formato correcto, and userName: El usuario es requerido"
    ]
    to:
    {
        field1 : errorMessage1,
        field2 : errorMessage2,
        field3 : errorMessage3,
    }
     */
    private Map<String, String> mapMethodArgumentNotValidExceptionMessage(Object[] message) {
        Map<String, String> mapResult = new HashMap<>();
        if (message != null && message.length < 2) {
            return mapResult;
        }
        assert message != null;
        String errorMessage = message[1].toString();
        String[] splitErrorMessage = errorMessage.split(",");
        for (String part : splitErrorMessage) {
            part = part.trim();
            if (part.startsWith("and ")) {
                part = part.substring(4).trim();
            }
            int colonIndex = part.indexOf(":");
            if (colonIndex != -1) {
                String key = part.substring(0, colonIndex).trim();
                String value = part.substring(colonIndex).trim();
                mapResult.put(key, value);
            }
        }
        return mapResult;
    }
}
