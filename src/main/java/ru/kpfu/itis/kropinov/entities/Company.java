package ru.kpfu.itis.kropinov.entities;

import ru.kpfu.itis.kropinov.enums.VerifyStatus;

public class Company {
    private Integer id;
    private Integer userId;
    private String companyName;
    private String inn;
    private VerifyStatus status;

    public Company(Integer userId, String companyName, String inn) {
        this.userId = userId;
        this.companyName = companyName;
        this.inn = inn;
        this.status = VerifyStatus.PENDING;
    }

    public Company(Integer id, Integer userId, String companyName, String inn, VerifyStatus status) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.inn = inn;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
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
}
