package ru.kpfu.itis.kropinov.servlets.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;

@WebServlet("/company/routes/check-route-number")
public class RouteNumberCheckServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RouteNumberCheckServlet.class);
    private RouteService routeService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String routeNumber = req.getParameter("routeNumber");
        String cityIdStr = req.getParameter("cityId");
        String transportModeIdStr = req.getParameter("transportModeId");

        if (routeNumber == null || cityIdStr == null || transportModeIdStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int cityId;
        int transportModeId;
        try {
            cityId = Integer.parseInt(cityIdStr);
            transportModeId = Integer.parseInt(transportModeIdStr);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        boolean isExists = false;
        isExists = routeService.routeNumberIsExists(transportModeId, cityId, routeNumber);

        resp.setContentType("application/json");
        Map<String, Boolean> responseData = Map.of("exists", isExists);
        mapper.writeValue(resp.getWriter(), responseData);

    }
}
