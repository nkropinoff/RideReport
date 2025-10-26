package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.City;

import java.util.List;

public interface CityDao {
    List<City> findAll();
}
