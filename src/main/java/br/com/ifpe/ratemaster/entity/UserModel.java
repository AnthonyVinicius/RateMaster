package br.com.ifpe.ratemaster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private long id;

    private String name;
    private String email;
    private String userType;
}
