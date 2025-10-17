package ru.kpfu.itis.kropinov.listeners;

import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dao.impl.UserDaoImpl;
import ru.kpfu.itis.kropinov.db.CustomConnectionPool;
import ru.kpfu.itis.kropinov.db.CustomDataSource;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.services.impl.UserServiceImpl;
import ru.kpfu.itis.kropinov.utils.PropertiesUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.util.Properties;

@WebListener
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Properties dbProperties = PropertiesUtil.getProperties("database.properties");
        CustomConnectionPool connectionPool = new CustomConnectionPool(
                dbProperties.getProperty("db.url"), dbProperties.getProperty("db.username"), dbProperties.getProperty("db.password"), Integer.parseInt(dbProperties.getProperty("db.poolsize"))
        );
        DataSource dataSource = new CustomDataSource(connectionPool);

        UserDao userDao = new UserDaoImpl(dataSource);
        UserService userService = new UserServiceImpl(userDao);
        sce.getServletContext().setAttribute("userService", userService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
