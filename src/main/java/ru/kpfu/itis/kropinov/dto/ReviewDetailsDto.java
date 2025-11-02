package ru.kpfu.itis.kropinov.dto;

import java.util.List;

public class ReviewDetailsDto {
    private String passengerEmail;
    private String rideTime;

    private String city;
    private String transportMode;
    private String route;
    private String vehicleNumber;

    private String text;

    private String photoUrl;

    private List<RatingItem> ratings;

    public void setRatings(List<RatingItem> ratings) {
        this.ratings = ratings;
    }

    public ReviewDetailsDto(String rideTime, String passengerEmail, String city, String transportMode, String route, String vehicleNumber, String text, String photoUrl) {
        this.rideTime = rideTime;
        this.passengerEmail = passengerEmail;
        this.city = city;
        this.transportMode = transportMode;
        this.route = route;
        this.vehicleNumber = vehicleNumber;
        this.text = text;
        this.photoUrl = photoUrl;
    }

    public ReviewDetailsDto(String passengerEmail, String rideTime, String city, String transportMode, String route, String vehicleNumber, String text, String photoUrl, List<RatingItem> ratings) {
        this.passengerEmail = passengerEmail;
        this.rideTime = rideTime;
        this.city = city;
        this.transportMode = transportMode;
        this.route = route;
        this.vehicleNumber = vehicleNumber;
        this.text = text;
        this.photoUrl = photoUrl;
        this.ratings = ratings;
    }

    public String getPassengerEmail() {
        return passengerEmail;
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

    public String getText() {
        return text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public List<RatingItem> getRatings() {
        return ratings;
    }
}
