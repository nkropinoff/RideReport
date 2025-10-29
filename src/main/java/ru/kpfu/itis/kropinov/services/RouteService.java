package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.RouteCreationDto;
import ru.kpfu.itis.kropinov.dto.RouteNumberDto;
import ru.kpfu.itis.kropinov.entities.City;
import ru.kpfu.itis.kropinov.entities.TransportMode;
import ru.kpfu.itis.kropinov.entities.Vehicle;

import java.util.List;

public interface RouteService {
    List<City> getAllCities();
    List<TransportMode> getAllTransportModes();
    boolean isVehicleNumberExists(String vehicleNumber);
    boolean routeNumberIsExists(int companyId, int cityId, String routeNumber);
    Result<Void> createRoute(RouteCreationDto dto);
    List<RouteNumberDto> getRouteNumbersByCompanyCityAndTransportMode(int companyId, int cityId, int transportModeId);
    List<Vehicle> getVehiclesByRouteIdAndCompanyId(int routeId, int companyId);
    void updateRouteVehicles(int routeId, List<Vehicle> vehicles, int companyId);
    void deleteRoute(int routeId, int companyId);
    List<RouteNumberDto> getRouteNumbersByCityAndTransportMode(int cityId, int transportModeId);
    List<Vehicle> getVehiclesByRouteId(int routeId);
}
