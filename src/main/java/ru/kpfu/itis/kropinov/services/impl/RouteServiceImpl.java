package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CityDao;
import ru.kpfu.itis.kropinov.dao.TransportModeDao;
import ru.kpfu.itis.kropinov.entities.City;
import ru.kpfu.itis.kropinov.entities.TransportMode;
import ru.kpfu.itis.kropinov.services.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private final static Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);

    private final CityDao cityDao;
    private final TransportModeDao transportModeDao;

    public RouteServiceImpl(CityDao cityDao, TransportModeDao transportModeDao) {
        this.cityDao = cityDao;
        this.transportModeDao = transportModeDao;
    }

    public List<City> getAllCities() {
        return cityDao.findAll();
    }

    public List<TransportMode> getAllTransportModes() {
        return transportModeDao.findAll();
    }


}
