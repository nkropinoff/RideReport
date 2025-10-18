package ru.kpfu.itis.kropinov.dto;

import ru.kpfu.itis.kropinov.enums.Role;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;

import java.io.Serializable;
import java.util.Optional;

public class UserSessionDto implements Serializable {
    private final int id;
    private final String email;
    private final Role role;
    private final CompanyInfo companyInfo;

    private UserSessionDto(int id, String email, Role role, CompanyInfo companyInfo) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.companyInfo = companyInfo;
    }

    public static UserSessionDto forPassenger(int id, String email) {
        return new UserSessionDto(id, email, Role.PASSENGER, null);
    }

    public static UserSessionDto forAdmin(int id, String email) {
        return new UserSessionDto(id, email, Role.ADMIN, null);
    }

    public static UserSessionDto forCompany(int id, String email, int companyId, String companyName, VerifyStatus status) {
        CompanyInfo companyInfo = new CompanyInfo(companyId, companyName, status);
        return new UserSessionDto(id, email, Role.COMPANY, companyInfo);
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Optional<CompanyInfo> getCompanyInfo() {
        return Optional.ofNullable(companyInfo);
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isCompany() {
        return role == Role.COMPANY;
    }

    public boolean isPassenger() {
        return role == Role.PASSENGER;
    }

    public static class CompanyInfo implements Serializable {
        private final int companyId;
        private final String companyName;
        private final VerifyStatus status;

        public CompanyInfo(int companyId, String companyName, VerifyStatus status) {
            this.companyId = companyId;
            this.companyName = companyName;
            this.status = status;
        }

        public int getCompanyId() {
            return companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public VerifyStatus getStatus() {
            return status;
        }

        public boolean isApproved() {
            return status == VerifyStatus.APPROVED;
        }

        public boolean isPending() {
            return status == VerifyStatus.PENDING;
        }

        public boolean isDenied() {
            return status == VerifyStatus.DENIED;
        }
    }
}
