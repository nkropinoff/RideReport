package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.Vehicle;

import java.sql.Connection;
import java.util.List;

public interface VehicleDao {
    boolean existsByVehicleNumber(String vehicleNumber);
    List<Vehicle> findVehiclesByRouteId(int routeId);
    void deleteVehicleNumberWithConnection(String vehicleNumber, Connection connection);
    List<Vehicle> findVehiclesByCompanyId(int companyId);
}
