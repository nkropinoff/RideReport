package ru.kpfu.itis.kropinov.dao;

import ru.kpfu.itis.kropinov.entities.ReviewPhoto;

import java.sql.Connection;

public interface ReviewPhotoDao {
    ReviewPhoto saveWithConnection(ReviewPhoto reviewPhoto, Connection connection);
}
