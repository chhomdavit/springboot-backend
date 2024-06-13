package com.davit.springblog.execption;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
		super(message);
	}
}
