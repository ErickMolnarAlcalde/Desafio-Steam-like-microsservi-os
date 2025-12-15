package com.desafio.user_and_wallet_service.controllers;

import com.desafio.user_and_wallet_service.dtos.LoginRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserResponseDto;
import com.desafio.user_and_wallet_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> create(@Validated @RequestBody UserRequestDto userRequestDto){
        UserResponseDto createdUser = userService.create(userRequestDto);

        // Cabeçalho com o endpoint do recurso recém criado
        URI location = URI.create("/user/" + createdUser.getId());

        //Irá devolver 201
        return ResponseEntity.created(location).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto requestDto){
        return ResponseEntity.ok().body(userService.login(requestDto));
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<UserResponseDto> getByEmail(@PathVariable String email){
        return ResponseEntity.ok().body(userService.getByEmail(email));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserResponseDto>> getAll(){
        return ResponseEntity.ok().body(userService.getAll());
    }

    @PatchMapping("/soft-delete")
    public ResponseEntity<Void> softDelete(@RequestBody LoginRequestDto requestDto){
        userService.softDelete(requestDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/alter")
    public ResponseEntity<UserResponseDto> alter(@RequestBody LoginRequestDto requestDto){
        return ResponseEntity.ok().body(userService.alter(requestDto));
    }



}
