package ru.kpfu.itis.kropinov.entities;

import ru.kpfu.itis.kropinov.enums.Role;

public class User {
    private Integer id;
    private String email;
    private String hashedPassword;
    private Role role;

    public User(String email, String hashedPassword, Role role) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public User(Integer id, String email, String hashedPassword, Role role) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
