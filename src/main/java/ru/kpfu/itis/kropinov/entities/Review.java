package ru.kpfu.itis.kropinov.entities;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int ownerId;
    private int routeId;
    private String vehicleNumber;
    private LocalDateTime rideTime;
    private String text;

    public Review(int id, int ownerId, int routeId, String vehicleNumber, LocalDateTime rideTime, String text) {
        this.id = id;
        this.ownerId = ownerId;
        this.routeId = routeId;
        this.vehicleNumber = vehicleNumber;
        this.rideTime = rideTime;
        this.text = text;
    }

    public Review(int ownerId, int routeId, String vehicleNumber, LocalDateTime rideTime, String text) {
        this.ownerId = ownerId;
        this.routeId = routeId;
        this.vehicleNumber = vehicleNumber;
        this.rideTime = rideTime;
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public LocalDateTime getRideTime() {
        return rideTime;
    }

    public String getText() {
        return text;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
