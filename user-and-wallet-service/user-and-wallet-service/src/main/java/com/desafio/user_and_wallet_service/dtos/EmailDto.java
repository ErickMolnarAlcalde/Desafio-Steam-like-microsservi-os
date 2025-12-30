package com.desafio.user_and_wallet_service.dtos;

import com.desafio.user_and_wallet_service.enums.StatusEmail;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    private String name;
    private String emailTo;
    private String emailFrom;
    private String emailSubject;
    private String emailText;
    private String timeStamp;
    private String statusEmail;


}
