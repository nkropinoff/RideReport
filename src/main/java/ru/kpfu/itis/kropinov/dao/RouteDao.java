package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Route;

import java.sql.Connection;
import java.util.List;

public interface RouteDao {
    boolean existsByTransportModeIdAndCityIdAndRouteNumber(int companyId, int cityId, String routeNumber);
    Route saveRouteWithConnection(Route route, Connection connection);
    void saveVehicleForRouteWithConnection(int routeId, String vehicle, Connection connection);
}
