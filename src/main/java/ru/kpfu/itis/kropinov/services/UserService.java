package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.*;

public interface UserService {
    Result<Void> registerPassenger(PassengerRegistrationDto dto);
    Result<Void> registerCompany(CompanyRegistrationDto dto);
    boolean isEmailTaken(String email);
    Result<UserSessionDto> login(UserLoginDto dto);
    int countAllCompanies();
    int countAllPassengers();
    Result<UserSessionDto> updateEmail(int userId, String email);
    Result<Void> updatePassword(int userId, String currentPassword, String newPassword, String confirmPassword);
}
