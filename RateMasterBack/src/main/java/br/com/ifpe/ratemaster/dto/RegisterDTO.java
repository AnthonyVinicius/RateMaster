package br.com.ifpe.ratemaster.dto;

import br.com.ifpe.ratemaster.entity.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
