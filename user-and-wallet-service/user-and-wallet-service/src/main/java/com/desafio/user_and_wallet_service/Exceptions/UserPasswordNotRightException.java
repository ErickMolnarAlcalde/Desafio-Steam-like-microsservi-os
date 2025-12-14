package com.desafio.user_and_wallet_service.Exceptions;

public class UserPasswordNotRightException extends RuntimeException{

    public UserPasswordNotRightException(String message) {
        super(message);
    }
}
