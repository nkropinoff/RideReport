package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;

public interface UserService {
    Result<Void> registerPassenger(String email, String password);
    Result<Void> registerCompany(String email, String password, String companyName, String inn);
    boolean isEmailTaken(String email);
    Result<UserSessionDto> login(String email, String password);
}
