package br.com.ifpe.ratemaster.dto;

import br.com.ifpe.ratemaster.entity.UserRole;

public record UserResponseDTO(String id, String name, String email,  String backGroundColor, String image, UserRole role) { }
