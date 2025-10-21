package ru.kpfu.itis.kropinov.dto;

import javax.servlet.http.Part;
import java.util.List;

public class CompanyRegistrationDto {
    private final String email;
    private final String password;
    private final String companyName;
    private final String inn;
    private final List<Part> companyDocuments;

    public CompanyRegistrationDto(String email, String password, String companyName, String inn, List<Part> companyDocuments) {
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.inn = inn;
        this.companyDocuments = companyDocuments;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getInn() {
        return inn;
    }

    public List<Part> getCompanyDocuments() {
        return companyDocuments;
    }
}
