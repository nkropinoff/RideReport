package ru.kpfu.itis.kropinov.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CustomConnectionPool {
    private final String url;
    private final String user;
    private final String password;
    private final int poolSize;
    private final BlockingQueue<Connection> pool;

    public CustomConnectionPool(String url, String user, String password, int poolSize) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;
        this.pool = new ArrayBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            pool.add(createConnection());
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
