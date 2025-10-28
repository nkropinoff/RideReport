package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.dto.RouteNumberDto;
import ru.kpfu.itis.kropinov.entities.Route;
import ru.kpfu.itis.kropinov.entities.Vehicle;

import java.sql.Connection;
import java.util.List;

public interface RouteDao {
    boolean existsByTransportModeIdAndCityIdAndRouteNumber(int companyId, int cityId, String routeNumber);
    Route saveRouteWithConnection(Route route, Connection connection);
    void saveVehicleForRouteWithConnection(int routeId, String vehicle, Connection connection);
    List<RouteNumberDto> findRouteNumbersByCompanyCityAndTransportMode(int companyId, int cityId, int transportModeId);
    boolean isRouteOwnedByCompany(int routeId, int companyId);
    void deleteRoute(int routeId);
}
