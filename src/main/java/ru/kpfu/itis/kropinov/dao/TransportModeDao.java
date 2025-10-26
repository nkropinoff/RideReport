package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.TransportMode;

import java.util.List;

public interface TransportModeDao {
    List<TransportMode> findAll();
}
