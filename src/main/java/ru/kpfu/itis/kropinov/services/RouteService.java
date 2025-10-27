package ru.kpfu.itis.kropinov.services;

import ru.kpfu.itis.kropinov.entities.City;
import ru.kpfu.itis.kropinov.entities.TransportMode;

import java.util.List;

public interface RouteService {
    List<City> getAllCities();
    List<TransportMode> getAllTransportModes();
    boolean isVehicleNumberExists(String vehicleNumber);
}
