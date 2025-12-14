package com.desafio.user_and_wallet_service.Exceptions;

public class UserNameNotFoundException extends RuntimeException{

    public UserNameNotFoundException(String message) {
        super(message);
    }

}
