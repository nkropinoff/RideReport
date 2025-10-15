package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.OperationResult;

public interface UserService {
    OperationResult registerPassenger(String email, String password);
    OperationResult registerCompany(String email, String password, String companyName, String inn);
}
