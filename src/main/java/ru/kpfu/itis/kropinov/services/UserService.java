package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.Result;

public interface UserService {
    Result<Void> registerPassenger(String email, String password);
    Result<Void> registerCompany(String email, String password, String companyName, String inn);
    boolean isEmailTaken(String email);
}
