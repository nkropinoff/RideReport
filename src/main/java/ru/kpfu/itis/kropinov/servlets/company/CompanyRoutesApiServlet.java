package ru.kpfu.itis.kropinov.servlets.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.kropinov.dto.RouteNumberDto;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/company/routes")
public class CompanyRoutesApiServlet extends HttpServlet {
    private RouteService routeService;
    private ObjectMapper mapper;

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

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if ( ((UserSessionDto) session.getAttribute("user")).getCompanyInfo().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int companyId = ((UserSessionDto) session.getAttribute("user")).getCompanyInfo().get().getCompanyId();

        List<RouteNumberDto> routeNumbers = routeService.getRouteNumbersByCompanyCityAndTransportMode(companyId, cityId, transportModeId);
        resp.setContentType("application/json");
        mapper.writeValue(resp.getWriter(), routeNumbers);
    }
}
