package com.italohreis.medly.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }


}
