package com.desafio.user_and_wallet_service.Exceptions;

public class UserEmailAlreadyExistsException extends RuntimeException{

    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}
