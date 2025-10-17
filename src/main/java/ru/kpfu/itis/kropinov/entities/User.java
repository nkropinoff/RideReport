package ru.kpfu.itis.kropinov.entities;

import ru.kpfu.itis.kropinov.enums.Role;

public class User {
    private String email;
    private String hashedPassword;
    private Role role;

    public User(String email, String hashedPassword, Role role) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Role getRole() {
        return role;
    }
}
