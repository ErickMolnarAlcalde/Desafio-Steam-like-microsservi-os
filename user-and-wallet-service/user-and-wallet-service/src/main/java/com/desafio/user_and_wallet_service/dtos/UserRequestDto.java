package com.desafio.user_and_wallet_service.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÀ-ú ]+$",
            message = "O nome deve conter apenas letras e espaços"
    )
    private String name;

    @NotBlank(message = "O email é obrigatório")
    @Email
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 64, message = "A senha deve ter entre 8 e 64 caracteres")
    private String password;

}
