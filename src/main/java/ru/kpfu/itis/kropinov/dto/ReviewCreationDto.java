package ru.kpfu.itis.kropinov.dto;

import javax.servlet.http.Part;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewCreationDto {
    private int userId;
    private int routeId;
    private String vehicleNumber;
    private LocalDateTime rideTime;
    private List<Integer> feedbackTagIds;
    private String reviewText;
    private Part photo;

    public ReviewCreationDto(int userId, int routeId, String vehicleNumber, LocalDateTime rideTime, List<Integer> feedbackTagIds, String reviewText, Part photo) {
        this.userId = userId;
        this.routeId = routeId;
        this.vehicleNumber = vehicleNumber;
        this.rideTime = rideTime;
        this.feedbackTagIds = feedbackTagIds;
        this.reviewText = reviewText;
        this.photo = photo;
    }

    public int getUserId() {
        return userId;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public LocalDateTime getRideTime() {
        return rideTime;
    }

    public List<Integer> getFeedbackTagIds() {
        return feedbackTagIds;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Part getPhoto() {
        return photo;
    }
}
