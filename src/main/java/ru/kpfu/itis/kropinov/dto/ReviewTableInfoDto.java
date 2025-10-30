package ru.kpfu.itis.kropinov.dto;

public class ReviewTableInfoDto {
    private final int reviewId;
    private final String rideTime;
    private final String city;
    private final String transportMode;
    private final String route;
    private final String vehicleNumber;
    private final String passengerEmail;

    public int getReviewId() {
        return reviewId;
    }

    public String getRideTime() {
        return rideTime;
    }

    public String getCity() {
        return city;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public String getRoute() {
        return route;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public ReviewTableInfoDto(int reviewId, String rideTime, String city, String transportMode, String route, String vehicleNumber, String passengerEmail) {
        this.reviewId = reviewId;
        this.rideTime = rideTime;
        this.city = city;
        this.transportMode = transportMode;
        this.route = route;
        this.vehicleNumber = vehicleNumber;
        this.passengerEmail = passengerEmail;
    }
}
