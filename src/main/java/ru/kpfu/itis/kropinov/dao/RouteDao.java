package ru.kpfu.itis.kropinov.dao;

public interface RouteDao {
    boolean existsByTransportModeIdAndCityIdAndRouteNumber(int companyId, int cityId, String routeNumber);
}
