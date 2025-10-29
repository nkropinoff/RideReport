package ru.kpfu.itis.kropinov.servlets.passenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.RouteNumberDto;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/routes")
public class RoutesApiServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(RoutesApiServlet.class);
    private ObjectMapper mapper;
    private RouteService routeService;

    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cityIdString = req.getParameter("cityId");
        String transportModeIdString = req.getParameter("transportModeId");

        if (cityIdString == null || cityIdString.isBlank() || transportModeIdString == null || transportModeIdString.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int cityId;
        int transportModeId;
        try {
            cityId = Integer.parseInt(cityIdString);
            transportModeId = Integer.parseInt(transportModeIdString);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<RouteNumberDto> routeNumbers = routeService.getRouteNumbersByCityAndTransportMode(cityId, transportModeId);
        resp.setContentType("application/json");
        mapper.writeValue(resp.getWriter(), routeNumbers);
    }
}
