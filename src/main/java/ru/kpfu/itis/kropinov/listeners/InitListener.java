package ru.kpfu.itis.kropinov.listeners;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.kropinov.config.CloudinaryConfig;
import ru.kpfu.itis.kropinov.dao.*;
import ru.kpfu.itis.kropinov.dao.impl.*;
import ru.kpfu.itis.kropinov.db.CustomConnectionPool;
import ru.kpfu.itis.kropinov.db.CustomDataSource;
import ru.kpfu.itis.kropinov.entities.Review;
import ru.kpfu.itis.kropinov.services.*;
import ru.kpfu.itis.kropinov.services.impl.*;
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
        RouteDao routeDao = new RouteDaoImpl(dataSource);
        FeedbackDao feedbackDao = new FeedbackDaoImpl(dataSource);
        ReviewDao reviewDao = new ReviewDaoImpl();
        ReviewPhotoDao reviewPhotoDao = new ReviewPhotoDaoImpl();

        RouteService routeService = new RouteServiceImpl(dataSource, cityDao, transportModeDao, vehicleDao, routeDao);
        FileStorageService fileStorageService = new FileStorageServiceImpl(cloudinary);
        UserService userService = new UserServiceImpl(dataSource, fileStorageService, userDao, companyDao, companyDocumentDao);
        CompanyService companyService = new CompanyServiceImpl(dataSource, companyDao, companyDocumentDao, userDao, fileStorageService);
        ReviewService reviewService = new ReviewServiceImpl(dataSource, fileStorageService, feedbackDao, reviewDao, reviewPhotoDao);

        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("companyService", companyService);
        sce.getServletContext().setAttribute("routeService", routeService);
        sce.getServletContext().setAttribute("reviewService", reviewService);

        sce.getServletContext().setAttribute("objectMapper", new ObjectMapper());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
