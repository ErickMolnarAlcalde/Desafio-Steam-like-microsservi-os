package com.desafio.user_and_wallet_service.Exceptions;

public class UserEmailNotfoundException extends RuntimeException{

    public UserEmailNotfoundException(String message) {
        super(message);
    }
}
