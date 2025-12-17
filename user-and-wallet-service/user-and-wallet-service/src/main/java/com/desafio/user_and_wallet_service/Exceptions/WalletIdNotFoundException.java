package com.desafio.user_and_wallet_service.Exceptions;

public class WalletIdNotFoundException extends RuntimeException{

    public WalletIdNotFoundException(String message) {
        super(message);
    }
}
