package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Vehicle;

import java.util.List;

public interface VehicleDao {
    boolean existsByVehicleNumber(String vehicleNumber);
    List<Vehicle> findVehiclesByRouteId(int routeId);
}
