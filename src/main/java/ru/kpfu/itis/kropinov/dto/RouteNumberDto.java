package ru.kpfu.itis.kropinov.dto;

public class RouteNumberDto {
    private int id;
    private String number;

    public RouteNumberDto(int id, String number) {
        this.id = id;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
