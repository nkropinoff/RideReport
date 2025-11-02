package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.CategoryStatDto;

import java.util.List;

public interface StatisticsService {
    List<CategoryStatDto> getCompanyStatistics(int companyId);
    List<CategoryStatDto> getVehicleNumberStatistics(int companyId, String vehicleNumber);
}
