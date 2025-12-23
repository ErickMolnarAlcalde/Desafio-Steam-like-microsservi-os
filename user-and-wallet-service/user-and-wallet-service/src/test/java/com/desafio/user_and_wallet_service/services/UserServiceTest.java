package com.desafio.user_and_wallet_service.services;

import com.desafio.user_and_wallet_service.Exceptions.UserEmailNotfoundException;
import com.desafio.user_and_wallet_service.Exceptions.UserPasswordNotRightException;
import com.desafio.user_and_wallet_service.dtos.LoginRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserRequestDto;
import com.desafio.user_and_wallet_service.dtos.UserResponseDto;
import com.desafio.user_and_wallet_service.entities.UserEntity;
import com.desafio.user_and_wallet_service.mappers.UserMapper;
import com.desafio.user_and_wallet_service.repositories.UserRepository;
import com.desafio.user_and_wallet_service.repositories.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ativa a injeção de mocks
class UserServiceTest {

    //todo mock cria um objeto falso
    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //cria uma instância real de UserService que vai utilizar os objetos falsos
    //isto permite só a lógica dos metodos service, sem acessar banco, mapper,etc..
    @InjectMocks
    private UserService userService;


    @Test
    void createShouldSaveUserAndReturnResponseDTO() {
        //Preparação dos dados (arrange)
        //Cria um DTO que simula o corpo da requisição de criação de usuário.
        UserRequestDto dto = new UserRequestDto(
                "Dona Maria Teste",
                "teste@email.com",
                "senhaforte123"
        );

        //cria uma entidade que receberá o DTO
        UserEntity userEntity = new UserEntity();
        userEntity.setIdUser(UUID.randomUUID());
        userEntity.setEmail(dto.getEmail());
        userEntity.setName(dto.getName());
        userEntity.setPassword(dto.getPassword());
        userEntity.setCreatedAt(LocalDateTime.now());

        //cria o objeto esperado após o mapeamento do dto
        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(userEntity.getIdUser())
                .name(userEntity.getName())
                .createdAt(userEntity.getCreatedAt())
                .build();

        //Configura mocks (simulando comprtamentos)
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodePass");
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toResponse(userEntity)).thenReturn(expectedResponse);

        //execução (act)
        UserResponseDto responseDto = userService.create(dto);

        //verificação (assert)
        assertEquals(expectedResponse.getId(), responseDto.getId());
        assertEquals(expectedResponse.getName(), responseDto.getName());
        assertEquals(expectedResponse.getCreatedAt(), responseDto.getCreatedAt());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void getByEmail_ShouldReturnUser_WhenEmailExists() {
        //arrange
        String email = "dona.maria@email.com";

        //cria uma entidade
        UserEntity userEntity = new UserEntity();
        userEntity.setIdUser(UUID.randomUUID());
        userEntity.setEmail(email);
        userEntity.setName("Dona Maria");
        userEntity.setCreatedAt(LocalDateTime.now());

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(userEntity.getIdUser())
                .name(userEntity.getName())
                .createdAt(userEntity.getCreatedAt())
                .build();

        //defini comprtamento dos mocks
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(expectedResponse);

        // ---------- Act ----------
        //esta chamando,executando o método que queremos chamar
        UserResponseDto actualResponse = userService.getByEmail(email);

        //---------- Assert ----------
        //verifica se o retorno esta igual o esperado
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getCreatedAt(), actualResponse.getCreatedAt());

        //confirma se os mocks foram chamados exatamente uma vez
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toResponse(userEntity);
    }

    @Test
    void getByEmail_ShouldNotReturnUser_WhenEmailNotExists(){
        String email = "falha@email.com";


        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserEmailNotfoundException.class,()->userService.getByEmail(email));


        verify(userRepository,times(1)).findByEmail(email);
        verify(userMapper,never()).toResponse(any());
    }


    @Test
    void login_ShouldReturnResponse_WhenEmailAndPasswordAreCorrect() {

        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "dona.maria@gmail.com",
                "mariaforte123"
        );

        UserEntity user = UserEntity.builder()
                .idUser(UUID.randomUUID())
                .name("Maria")
                .email(loginRequestDto.getEmail())
                .password(loginRequestDto.getPassword())
                .createdAt(LocalDateTime.now())
                .build();

        UserResponseDto expected = UserResponseDto.builder()
                .id(user.getIdUser())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .build();

        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(userMapper.toResponse(user)).thenReturn(expected);


        UserResponseDto result = userService.login(loginRequestDto);


        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCreatedAt(), result.getCreatedAt());

        verify(userRepository).findByEmail(loginRequestDto.getEmail());
        verify(userMapper).toResponse(user);
    }

    @Test
    void softDelete() {

    }

    @Test
    void alter() {
    }
}