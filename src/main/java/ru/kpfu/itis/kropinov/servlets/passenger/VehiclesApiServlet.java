package ru.kpfu.itis.kropinov.servlets.passenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.entities.Vehicle;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/vehicles")
public class VehiclesApiServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(VehiclesApiServlet.class);
    private ObjectMapper mapper;
    private RouteService routeService;

    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String routeIdString = req.getParameter("routeId");

        if (routeIdString == null || routeIdString.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int routeId;
        try {
            routeId = Integer.parseInt(routeIdString);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<String> vehicleNumbers = routeService.getVehiclesByRouteId(routeId).stream().map(Vehicle::getNumber).toList();
        resp.setContentType("application/json");
        mapper.writeValue(resp.getWriter(), vehicleNumbers);
    }
}
