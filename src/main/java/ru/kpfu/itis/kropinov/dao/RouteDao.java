package ru.kpfu.itis.kropinov.dao;

public interface RouteDao {
    boolean existsByCompanyIdAndCityIdAndRouteNumber(int companyId, int cityId, String routeNumber);
}
