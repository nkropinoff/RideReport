package ru.kpfu.itis.kropinov.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CustomConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(CustomConnectionPool.class);

    private final String url;
    private final String user;
    private final String password;
    private final int poolSize;
    private final BlockingQueue<Connection> pool;

    public CustomConnectionPool(String url, String user, String password, int poolSize) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;
        this.pool = new ArrayBlockingQueue<>(poolSize);

        try {
            Class.forName("org.postgresql.Driver");
            for (int i = 0; i < poolSize; i++) pool.add(createConnection());
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Failed to create connection pool", e);
            throw new RuntimeException("Failed to create connection pool", e);
        }
    }

    private Connection createConnection() throws SQLException {
        Connection realConnection = DriverManager.getConnection(url, user, password);
        return new PooledConnection(realConnection, this);
    }

    public Connection getConnection() throws SQLException {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for a connection", e);
        }
    }

    void realiseConnection(Connection connection) {
        pool.offer(connection);
    }

}
