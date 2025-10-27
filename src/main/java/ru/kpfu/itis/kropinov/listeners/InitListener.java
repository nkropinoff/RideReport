package ru.kpfu.itis.kropinov.listeners;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.kropinov.config.CloudinaryConfig;
import ru.kpfu.itis.kropinov.dao.*;
import ru.kpfu.itis.kropinov.dao.impl.*;
import ru.kpfu.itis.kropinov.db.CustomConnectionPool;
import ru.kpfu.itis.kropinov.db.CustomDataSource;
import ru.kpfu.itis.kropinov.services.CompanyService;
import ru.kpfu.itis.kropinov.services.FileStorageService;
import ru.kpfu.itis.kropinov.services.RouteService;
import ru.kpfu.itis.kropinov.services.UserService;
import ru.kpfu.itis.kropinov.services.impl.CompanyServiceImpl;
import ru.kpfu.itis.kropinov.services.impl.FileStorageServiceImpl;
import ru.kpfu.itis.kropinov.services.impl.RouteServiceImpl;
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

        Properties cloudinaryProperties = PropertiesUtil.getProperties("cloudinary.properties");
        Cloudinary cloudinary = CloudinaryConfig.createCloudinary(cloudinaryProperties);

        CityDao cityDao = new CityDaoImpl(dataSource);
        TransportModeDao transportModeDao = new TransportModeDaoImpl(dataSource);
        CompanyDao companyDao = new CompanyDaoImpl(dataSource);
        CompanyDocumentDao companyDocumentDao = new CompanyDocumentDaoImpl(dataSource);
        UserDao userDao = new UserDaoImpl(dataSource);
        VehicleDao vehicleDao = new VehicleDaoImpl(dataSource);

        RouteService routeService = new RouteServiceImpl(cityDao, transportModeDao, vehicleDao);
        FileStorageService fileStorageService = new FileStorageServiceImpl(cloudinary);
        UserService userService = new UserServiceImpl(dataSource, fileStorageService, userDao, companyDao, companyDocumentDao);
        CompanyService companyService = new CompanyServiceImpl(dataSource, companyDao, companyDocumentDao, userDao, fileStorageService);

        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("companyService", companyService);
        sce.getServletContext().setAttribute("routeService", routeService);

        sce.getServletContext().setAttribute("objectMapper", new ObjectMapper());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
