package ru.kpfu.itis.kropinov.dto;

import java.util.List;

public class RouteCreationDto {
    private final int companyId;
    private final int cityId;
    private final int transportModeId;
    private final String routeNumber;
    private final List<String> vehicles;

    public RouteCreationDto(int companyId, int cityId, int transportModeId, String routeNumber, List<String> vehicles) {
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

    public List<String> getVehicles() {
        return vehicles;
    }
}
