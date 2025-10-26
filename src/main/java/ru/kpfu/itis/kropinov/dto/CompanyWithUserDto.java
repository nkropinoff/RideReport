package ru.kpfu.itis.kropinov.dto;

import ru.kpfu.itis.kropinov.enums.VerifyStatus;

public class CompanyWithUserDto {
    private Integer id;
    private String companyName;
    private String inn;
    private VerifyStatus status;
    private String userEmail;

    public Integer getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getInn() {
        return inn;
    }

    public VerifyStatus getStatus() {
        return status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public CompanyWithUserDto(Integer id, String companyName, String inn, VerifyStatus status, String userEmail) {
        this.id = id;
        this.companyName = companyName;
        this.inn = inn;
        this.status = status;
        this.userEmail = userEmail;
    }
}
