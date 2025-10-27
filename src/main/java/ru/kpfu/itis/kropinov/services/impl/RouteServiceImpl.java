package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CityDao;
import ru.kpfu.itis.kropinov.dao.RouteDao;
import ru.kpfu.itis.kropinov.dao.TransportModeDao;
import ru.kpfu.itis.kropinov.dao.VehicleDao;
import ru.kpfu.itis.kropinov.entities.City;
import ru.kpfu.itis.kropinov.entities.TransportMode;
import ru.kpfu.itis.kropinov.services.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private final static Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);

    private final CityDao cityDao;
    private final TransportModeDao transportModeDao;
    private final VehicleDao vehicleDao;
    private final RouteDao routeDao;

    public RouteServiceImpl(CityDao cityDao, TransportModeDao transportModeDao, VehicleDao vehicleDao, RouteDao routeDao) {
        this.cityDao = cityDao;
        this.transportModeDao = transportModeDao;
        this.vehicleDao = vehicleDao;
        this.routeDao = routeDao;
    }

    public List<City> getAllCities() {
        return cityDao.findAll();
    }

    public List<TransportMode> getAllTransportModes() {
        return transportModeDao.findAll();
    }

    @Override
    public boolean isVehicleNumberExists(String vehicleNumber) {
        return vehicleDao.existsByVehicleNumber(vehicleNumber);
    }

    @Override
    public boolean routeNumberIsExists(int companyId, int cityId, String routeNumber) {
        return routeDao.existsByCompanyIdAndCityIdAndRouteNumber(companyId, cityId, routeNumber);
    }
}
