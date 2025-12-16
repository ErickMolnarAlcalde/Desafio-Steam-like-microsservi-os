package com.desafio.user_and_wallet_service.services;

import com.desafio.user_and_wallet_service.Exceptions.UserEmailAlreadyExistsException;
import com.desafio.user_and_wallet_service.Exceptions.UserEmailNotfoundException;
import com.desafio.user_and_wallet_service.Exceptions.UserPasswordNotRightException;
import com.desafio.user_and_wallet_service.Exceptions.WalletIdNotFoundException;
import com.desafio.user_and_wallet_service.dtos.LoginRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserResponseDto;
import com.desafio.user_and_wallet_service.entities.UserEntity;
import com.desafio.user_and_wallet_service.entities.WalletEntity;
import com.desafio.user_and_wallet_service.mappers.UserMapper;
import com.desafio.user_and_wallet_service.repositories.UserRepository;
import com.desafio.user_and_wallet_service.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final WalletRepository walletRepository;

    public UserResponseDto create(UserRequestDto userRequestDto){
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new UserEmailAlreadyExistsException("Email já cadastrado!");
        }
        var entity = userMapper.toEntity(userRequestDto);
        entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
        var savedEntity = userRepository.save(entity);
        return userMapper.toResponse(savedEntity);
    }

    public UserResponseDto getByEmail(String email){
        var entity = userRepository.findByEmail(email).orElseThrow(()->
                new UserEmailNotfoundException("email não encontrado!"));
        return userMapper.toResponse(entity);
    }

    public List<UserResponseDto> getAll(){
        List<UserEntity> listUsers = userRepository.findAll();
        List<UserResponseDto> dtos = listUsers.stream().map(userMapper::toResponse).toList();
        return dtos;
    }


    public UserResponseDto login(LoginRequestDto login){
        var entity = userRepository.findByEmail(login.getEmail()).orElseThrow(()->
                new UserEmailNotfoundException("Email não encontrado!"));
        if(!(bCryptPasswordEncoder.matches(login.getPassword(), entity.getPassword()))){
            throw new UserPasswordNotRightException("Senha inválida!");
        }
       return userMapper.toResponse(entity);
    }

    public void softDelete(LoginRequestDto login){
        var entity = userRepository.findByEmail(login.getEmail()).orElseThrow(()->
                new UserEmailNotfoundException("Email não encontrado!"));
        if(!(bCryptPasswordEncoder.matches(login.getPassword(), entity.getPassword()))){
            throw new UserPasswordNotRightException("Senha inválida!");
        }
        var walletiD = entity.getWallet();

        entity.setDeleted(true);
        walletiD.setDeleted(true);
        userRepository.save(entity);
    }

    public UserResponseDto alter(LoginRequestDto login){
        var entity = userRepository.findByEmail(login.getEmail()).orElseThrow(()->
                new UserEmailNotfoundException("Email não encontrado!"));
        if(!(bCryptPasswordEncoder.matches(login.getPassword(), entity.getPassword()))){
            throw new UserPasswordNotRightException("Senha inválida!");
        }
        var alteredEntity = userMapper.toAlter(login);
        userRepository.save(alteredEntity);

        return userMapper.toResponse(alteredEntity);
    }






}
