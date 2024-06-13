package com.davit.springblog.execption;

public class EmptyOrNotNullException extends RuntimeException{

    public EmptyOrNotNullException(String message) {
		super(message);
	}
}
