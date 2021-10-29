package com.unicon.unicon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author brahnavan
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Methods to create exception
	 * @param message
	 */
	public MyFileNotFoundException(String message) {
        super(message);
    }

	/**
	 * Method to create exception with message and cause
	 * @param message
	 * @param cause
	 */
    public MyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}