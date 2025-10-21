package ru.kpfu.itis.kropinov.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.kropinov.dao.CompanyDao;
import ru.kpfu.itis.kropinov.dao.CompanyDocumentDao;
import ru.kpfu.itis.kropinov.dao.UserDao;
import ru.kpfu.itis.kropinov.dao.impl.CompanyDaoImpl;
import ru.kpfu.itis.kropinov.dao.impl.CompanyDocumentDaoImpl;
import ru.kpfu.itis.kropinov.dao.impl.UserDaoImpl;
import ru.kpfu.itis.kropinov.db.CustomConnectionPool;
import ru.kpfu.itis.kropinov.db.CustomDataSource;
import ru.kpfu.itis.kropinov.services.FileStorageService;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.services.impl.FileStorageServiceImpl;
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

        CompanyDao companyDao = new CompanyDaoImpl(dataSource);
        CompanyDocumentDao companyDocumentDao = new CompanyDocumentDaoImpl(dataSource);
        UserDao userDao = new UserDaoImpl(dataSource);

        FileStorageService fileStorageService = new FileStorageServiceImpl();
        UserService userService = new UserServiceImpl(dataSource, fileStorageService, userDao, companyDao, companyDocumentDao);
        sce.getServletContext().setAttribute("userService", userService);

        sce.getServletContext().setAttribute("objectMapper", new ObjectMapper());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
