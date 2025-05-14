package br.com.ifpe.ratemaster.entity;

public enum UserRole {
    BUSINESS("business"),
    INDIVIDUAL("individual");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
