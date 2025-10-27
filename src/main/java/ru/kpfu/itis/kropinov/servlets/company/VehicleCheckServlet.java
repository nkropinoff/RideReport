package ru.kpfu.itis.kropinov.servlets.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/company/routes/check-vehicle")
public class VehicleCheckServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(VehicleCheckServlet.class);
    private RouteService routeService;
    private ObjectMapper mapper;


    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String vehicleNumber = req.getParameter("vehicleNumber");

        boolean isExists = false;
        if (vehicleNumber != null && !vehicleNumber.isBlank()) {
            isExists = routeService.isVehicleNumberExists(vehicleNumber);
        }

        resp.setContentType("application/json");
        Map<String,Boolean> responseData = Map.of("exists", isExists);
        mapper.writeValue(resp.getWriter(), responseData);
    }
}
