package org.edupoll.config;

import org.edupoll.exception.ExistUserEmailException;
import org.edupoll.exception.InvalidPasswordException;
import org.edupoll.exception.NotExistUserException;
import org.edupoll.exception.VerifyCodeException;
import org.edupoll.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;

@ControllerAdvice
public class ExceptionHandlerConfiguration {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Void> exceptionHandle(MethodArgumentNotValidException ex) {
		
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(ExistUserEmailException.class)
	public ResponseEntity<Void> exceptionHandle(ExistUserEmailException ex) {

		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Void> exceptionHandle(InvalidPasswordException ex) {

		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(VerifyCodeException.class)
	public ResponseEntity<ErrorResponse> exceptionHandle(VerifyCodeException ex) {
		ErrorResponse response = new ErrorResponse(400, ex.getMessage(),System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotExistUserException.class)
	public ResponseEntity<Void> exceptionHandle(NotExistUserException ex) {

		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler({JWTDecodeException.class, TokenExpiredException.class})
	public ResponseEntity<ErrorResponse> jwtExceptionHandle(Exception ex) {
		var response = 
				new ErrorResponse(401, "token value is expired or damaged", System.currentTimeMillis());
		
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
}