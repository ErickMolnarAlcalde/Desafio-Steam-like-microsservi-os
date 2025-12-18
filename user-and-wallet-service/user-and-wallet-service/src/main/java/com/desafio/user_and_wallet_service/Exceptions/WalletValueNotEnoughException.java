package com.desafio.user_and_wallet_service.Exceptions;

public class WalletValueNotEnoughException extends RuntimeException{

    public WalletValueNotEnoughException(String message) {
        super(message);
    }

}
