package com.ingesup.exception;

@SuppressWarnings("serial")
public class DaoException extends RuntimeException {
    private int code;

    public int getCode() {
	return code;
    }

    public DaoException(String message, int code) {
	super(message);
	this.code = code;
    }

}
