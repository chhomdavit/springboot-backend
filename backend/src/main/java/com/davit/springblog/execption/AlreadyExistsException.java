package com.davit.springblog.execption;

public class AlreadyExistsException extends RuntimeException{

    public AlreadyExistsException(String message) {
		super(message);
	}
}
