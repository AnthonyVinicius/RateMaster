package br.com.ifpe.ratemaster.dto;

import br.com.ifpe.ratemaster.entity.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
