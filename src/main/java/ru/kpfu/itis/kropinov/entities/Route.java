package ru.kpfu.itis.kropinov.entities;

public class Route {
    private int id;
    private int companyId;
    private int cityId;
    private int transportModeId;
    private String routeNumber;

    public Route(int companyId, int cityId, int transportModeId, String routeNumber) {
        this.companyId = companyId;
        this.cityId = cityId;
        this.transportModeId = transportModeId;
        this.routeNumber = routeNumber;
    }

    public Route(int id, int companyId, int cityId, int transportModeId, String routeNumber) {
        this.id = id;
        this.companyId = companyId;
        this.cityId = cityId;
        this.transportModeId = transportModeId;
        this.routeNumber = routeNumber;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }
}
