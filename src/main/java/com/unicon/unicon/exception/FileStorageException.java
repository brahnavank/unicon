package com.unicon.unicon.exception;

/**
 * 
 * @author brahnavan
 *
 */
public class FileStorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Methods to create exception
	 * @param message
	 */
	public FileStorageException(String message) {
        super(message);
    }

	/**
	 * Method to create exception with message and cause
	 * @param message
	 * @param cause
	 */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
