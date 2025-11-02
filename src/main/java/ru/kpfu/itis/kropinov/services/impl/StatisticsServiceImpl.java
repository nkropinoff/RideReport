package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.StatisticsDao;
import ru.kpfu.itis.kropinov.dao.VehicleDao;
import ru.kpfu.itis.kropinov.dto.CategoryStatDto;
import ru.kpfu.itis.kropinov.exceptions.AccessDeniedException;
import ru.kpfu.itis.kropinov.services.StatisticsService;

import java.util.List;

public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    private StatisticsDao statisticsDao;
    private VehicleDao vehicleDao;

    public StatisticsServiceImpl(VehicleDao vehicleDao, StatisticsDao statisticsDao) {
        this.vehicleDao = vehicleDao;
        this.statisticsDao = statisticsDao;
    }

    @Override
    public List<CategoryStatDto> getCompanyStatistics(int companyId) {
        return statisticsDao.getCompanyStatistics(companyId);
    }

    @Override
    public List<CategoryStatDto> getVehicleNumberStatistics(int companyId, String vehicleNumber) {
        if (!vehicleDao.isVehicleOwnedByCompany(companyId, vehicleNumber)) {
            logger.warn("Vehicle number: {} is not owned by company with id: {}", vehicleNumber, companyId);
            throw new AccessDeniedException("Vehicle number is not owned by company");
        }

        return statisticsDao.getVehicleStatistics(vehicleNumber);
    }
}
