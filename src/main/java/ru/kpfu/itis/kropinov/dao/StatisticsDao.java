package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.dto.CategoryStatDto;

import java.util.List;

public interface StatisticsDao {
    List<CategoryStatDto> getCompanyStatistics(int companyId);
    List<CategoryStatDto> getVehicleStatistics(String vehicleNumber);
}
