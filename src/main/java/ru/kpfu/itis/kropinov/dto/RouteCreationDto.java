package ru.kpfu.itis.kropinov.dto;

import ru.kpfu.itis.kropinov.entities.Vehicle;

import java.util.List;

public class RouteCreationDto {
    private final int companyId;
    private final int cityId;
    private final int transportModeId;
    private final String routeNumber;
    private final List<Vehicle> vehicles;

    public RouteCreationDto(int companyId, int cityId, int transportModeId, String routeNumber, List<Vehicle> vehicles) {
        this.companyId = companyId;
        this.cityId = cityId;
        this.transportModeId = transportModeId;
        this.routeNumber = routeNumber;
        this.vehicles = vehicles;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getCityId() {
        return cityId;
    }

    public int getTransportModeId() {
        return transportModeId;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}
