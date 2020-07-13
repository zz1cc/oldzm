package com.zm.platform.common.exception.runtime;

public class NotLoginException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotLoginException(String message) {
        super(message);
    }
}
