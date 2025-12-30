package com.desafio.user_and_wallet_service.entities;

import com.desafio.user_and_wallet_service.enums.StatusEmail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class EmailModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idEmail;

    private String name;
    private String emailTo;
    private String emailFrom;
    private String emailSubject;
    @Column(columnDefinition = "TEXT")
    private String emailText;
    private LocalDateTime timeStamp;
    private StatusEmail statusEmail;


}
