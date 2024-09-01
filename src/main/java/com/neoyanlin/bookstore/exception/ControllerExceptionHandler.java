package com.neoyanlin.bookstore.exception;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ApiResponse> handleException(ResourceNotFoundException exception) {
		ApiResponse error = new ApiResponse();
		
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exception.getMessage());
		error.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiResponse> handleException(BadRequestException exception, WebRequest request) {
		ApiResponse error = new ApiResponse();
		
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();

        String message = getCustomMessageForMethod(method, "an issue with the request. " + exception.getMessage());
		
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(message);
		error.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class) 
    public ResponseEntity<ApiResponse> handleValidationException(ConstraintViolationException exception, WebRequest request) {
		
		ApiResponse error = new ApiResponse();
		
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();

        String message = getCustomMessageForMethod(method, "an issue with the request. " + formatValidationErrors(exception.getConstraintViolations()));

		error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		error.setMessage(message);
		error.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException exception, WebRequest request) {
		
		ApiResponse error = new ApiResponse();
		
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();
        String errorMessage = "The field '";
        
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
        	errorMessage += fieldError.getField() + "' " + fieldError.getDefaultMessage();
        }
        
        String message = getCustomMessageForMethod(method, "an issue with the request. " + errorMessage);

		error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		error.setMessage(message);
		error.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
	
	@ExceptionHandler
	public ResponseEntity<ApiResponse> handleException(Exception exception, WebRequest request) {
		
		ApiResponse error = new ApiResponse();
		
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String method = servletWebRequest.getHttpMethod().toString();

        String message = getCustomMessageForMethod(method, "an issue with the server.");
		
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setMessage(message);
		error.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private String getCustomMessageForMethod(String method, String errorType) {
	
		String message;
		switch (method) {
		    case "POST":
		    	message = "Book cannot be created due to " + errorType;
		    	break;
		    case "PUT":
		    	message = "Book cannot be updated due to " + errorType;
		    	break;
		    case "DELETE":
		    	message = "Book cannot be deleted due to " + errorType;
		    	break;
		    case "GET":
		    	message = "Book cannot be retrieved due to " + errorType;
		    	break;
		    default:
		    	message = "An unexpected error occurred.";
		    	break;
		}
		
		return message;
    }
	
	public static String formatValidationErrors(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(violation -> "The field '" + violation.getPropertyPath() + "' " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }
}
