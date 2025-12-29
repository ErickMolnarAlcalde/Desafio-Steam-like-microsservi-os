package com.desafio.user_and_wallet_service.Exceptions;

import com.desafio.user_and_wallet_service.dtos.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerException {

    @ExceptionHandler(UserEmailNotfoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto emailNotfoundException(UserEmailNotfoundException ex){
        ErrorDto errorDto = new ErrorDto(
                ex.getMessage(),
                "Verificar se o email está correto",
                LocalDateTime.now()
        );
        return errorDto;
    }

    @ExceptionHandler(UserNameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto userNameNotFoundException (UserNameNotFoundException ex){
        ErrorDto errorDto = new ErrorDto(
                ex.getMessage(),
                "Verificar se o nome está correto",
                LocalDateTime.now()
        );
        return errorDto;
    }

    @ExceptionHandler(UserPasswordNotRightException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto userPasswordNotRightException(UserPasswordNotRightException ex){
        ErrorDto errorDto = new ErrorDto(
                ex.getMessage(),
                "Veririficar se a senha foi digitada corretamente",
                LocalDateTime.now()
        );
        return errorDto;
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto userEmailAlreadyExistsException(UserEmailAlreadyExistsException ex){
        ErrorDto errorDto = new ErrorDto(
                ex.getMessage(),
                "Por gentileza, cadastrar conta com um e-mail diferente",
                LocalDateTime.now()
        );
        return errorDto;
    }

    @ExceptionHandler(WalletIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto walletIdNotFoundException(WalletIdNotFoundException ex){
        ErrorDto errorDto = new ErrorDto(
                ex.getMessage(),
               "Nenhuma carteira foi localizada com o ID informado",
                LocalDateTime.now()
        );
        return errorDto;
    }

    @ExceptionHandler(WalletValueNotEnoughException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorDto walletValueNotEnoughException(WalletValueNotEnoughException ex){
        ErrorDto errorDto = new ErrorDto(
                ex.getMessage(),
                "Saldo insuficiente para completar a transação",
                LocalDateTime.now()
        );
        return errorDto;
    }




    //testa erro de integridade
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrity(DataIntegrityViolationException ex){

        String message = "Violação de integridade: valor duplicado detectado.";

        if(ex.getMessage().contains("tb_user_email_key")){
            message = "o email informado já está cadastrado";
        }

        ErrorDto errorDto = new ErrorDto(
                message,
                "Verifique os dados e tente novamente.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }



}
