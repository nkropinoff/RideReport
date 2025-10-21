package ru.kpfu.itis.kropinov.dto;

public class PassengerRegistrationDto {
    private final String email;
    private final String password;

    public PassengerRegistrationDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
