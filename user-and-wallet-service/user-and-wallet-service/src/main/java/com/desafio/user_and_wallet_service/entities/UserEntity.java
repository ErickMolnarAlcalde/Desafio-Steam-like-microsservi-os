package com.desafio.user_and_wallet_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user")
    private UUID idUser;

    @Column(name = "name",nullable = false,length = 100)
    private String name;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "password",nullable = false, length = 100)
    private String password;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user",cascade = CascadeType.PERSIST)
    private WalletEntity wallet;

    @Builder.Default
    private Boolean deleted = false;


}
