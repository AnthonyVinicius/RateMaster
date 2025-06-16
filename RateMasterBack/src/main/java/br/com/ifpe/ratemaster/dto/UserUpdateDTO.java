package br.com.ifpe.ratemaster.dto;

public record UserUpdateDTO(
        String name,
        String image,
        String backGroundColor
) {}