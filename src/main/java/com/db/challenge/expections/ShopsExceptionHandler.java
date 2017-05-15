package com.db.challenge.expections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Francisco San Roman
 * 
 *         Custom Exception Handler
 *
 */
@ControllerAdvice
public class ShopsExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflictBadRequest(RuntimeException ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setMessage("Illegal Argument in  Rest Shops API: " + ex.getMessage());
		errorMessage.setCode(HttpStatus.BAD_REQUEST.toString());
		return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(value = { ShopNotFoundException.class})
	protected ResponseEntity<Object> haddleNotFound(RuntimeException ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setMessage("Shop not found");
		errorMessage.setCode(HttpStatus.NOT_FOUND.toString());
		return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Object> handleConflictRuntime(RuntimeException ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setMessage("Error in  Rest Shops API: " + ex.getMessage());
		errorMessage.setCode(HttpStatus.SERVICE_UNAVAILABLE.toString());
		return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<Object> handleConflictOther(RuntimeException ex, WebRequest request) {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setMessage("Error in  Rest Shops API: " + ex.getMessage());
		errorMessage.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}