package ru.kpfu.itis.kropinov.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dao.CityDao;
import ru.kpfu.itis.kropinov.dao.RouteDao;
import ru.kpfu.itis.kropinov.dao.TransportModeDao;
import ru.kpfu.itis.kropinov.dao.VehicleDao;
import ru.kpfu.itis.kropinov.db.CustomDataSource;
import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.RouteCreationDto;
import ru.kpfu.itis.kropinov.dto.RouteNumberDto;
import ru.kpfu.itis.kropinov.entities.City;
import ru.kpfu.itis.kropinov.entities.Route;
import ru.kpfu.itis.kropinov.entities.TransportMode;
import ru.kpfu.itis.kropinov.entities.Vehicle;
import ru.kpfu.itis.kropinov.exceptions.AccessDeniedException;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.accessibility.AccessibleSelection;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private final static Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);
    private final DataSource ds;

    private final CityDao cityDao;
    private final TransportModeDao transportModeDao;
    private final VehicleDao vehicleDao;
    private final RouteDao routeDao;

    public RouteServiceImpl(DataSource ds, CityDao cityDao, TransportModeDao transportModeDao, VehicleDao vehicleDao, RouteDao routeDao) {
        this.cityDao = cityDao;
        this.transportModeDao = transportModeDao;
        this.vehicleDao = vehicleDao;
        this.routeDao = routeDao;
        this.ds = ds;
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
    public boolean routeNumberIsExists(int transportModeId, int cityId, String routeNumber) {
        return routeDao.existsByTransportModeIdAndCityIdAndRouteNumber(transportModeId, cityId, routeNumber);
    }

    @Override
    public Result<Void> createRoute(RouteCreationDto dto) {
        String errorMessage = validateRouteData(dto);
        if (errorMessage != null) return Result.error(errorMessage);

        Route newRoute = new Route(dto.getCompanyId(), dto.getCityId(), dto.getTransportModeId(), dto.getRouteNumber());

        try (Connection connection = ds.getConnection()) {
            try {
                connection.setAutoCommit(false);

                Route savedRoute = routeDao.saveRouteWithConnection(newRoute, connection);
                for (Vehicle vehicle : dto.getVehicles()) {
                    routeDao.saveVehicleForRouteWithConnection(savedRoute.getId(), vehicle.getNumber(), connection);
                }

                connection.commit();
                return Result.success();
            } catch (SQLException e) {
                CustomDataSource.rollback(connection);
                logger.error("Failed create route", e);
                throw new DataAccessException("Failed create route", e);
            }
        } catch (SQLException e) {
            logger.error("Could not obtain connection", e);
            throw new DataAccessException("Could not obtain connection", e);
        }
    }

    private String validateRouteData(RouteCreationDto dto) {

        if (routeNumberIsExists(dto.getTransportModeId(), dto.getCityId(), dto.getRouteNumber())) return "Маршрут с таким номером для этого типа транспорта уже существует в этом городе";

        for (Vehicle vehicle : dto.getVehicles()) {
            if (vehicle.getNumber().length() > 20) return "Номер ТС не должен превышать 20 символов.";
            if (isVehicleNumberExists(vehicle.getNumber())) return "Этот номер ТС уже используется в другом маршруте";
        }

        return null;
    }

    @Override
    public List<RouteNumberDto> getRouteNumbersByCompanyCityAndTransportMode(int companyId, int cityId, int transportModeId) {
        return routeDao.findRouteNumbersByCompanyCityAndTransportMode(companyId, cityId, transportModeId);
    }

    @Override
    public List<Vehicle> getVehiclesByRouteId(int routeId, int companyId) {
        if (!routeDao.isRouteOwnedByCompany(routeId, companyId)) {
            throw new AccessDeniedException("Route is not owned by this company.");
        }

        return vehicleDao.findVehiclesByRouteId(routeId);
    }
}
