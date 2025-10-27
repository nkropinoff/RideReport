package ru.kpfu.itis.kropinov.servlets.company;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.RouteCreationDto;
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

@WebServlet("/company/routes")
public class CompanyRoutesServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(CompanyRoutesServlet.class);
    private RouteService routeService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cityIdString = req.getParameter("cityId");
        String transportModeIdString = req.getParameter("transportModeId");
        String routeNumber = req.getParameter("routeNumber");
        String vehiclesJson = req.getParameter("vehicles");

        req.setAttribute("cityId", cityIdString);
        req.setAttribute("transportModeId", transportModeIdString);
        req.setAttribute("routeNumber", routeNumber);
        req.setAttribute("vehicles", vehiclesJson);

        if (cityIdString == null || cityIdString.isBlank() || transportModeIdString == null || transportModeIdString.isBlank() ||
            routeNumber == null || routeNumber.isBlank()) {
            req.setAttribute("error", "Поля 'Город', 'Тип транспорта', 'Номер маршрута' должны быть заполнены");
            req.getRequestDispatcher("route_new.ftl").forward(req, resp);
            return;
        }

        if (vehiclesJson == null || vehiclesJson.isBlank()) {
            req.setAttribute("error", "Добавьте хотя бы одно ТС, обслуживающее этот маршрут.");
            req.getRequestDispatcher("route_new.ftl").forward(req, resp);
            return;
        }

        int cityId;
        int transportModeId;
        try {
            cityId = Integer.parseInt(cityIdString);
            transportModeId = Integer.parseInt(transportModeIdString);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Некорректный формат данных");
            req.getRequestDispatcher("route_new").forward(req, resp);
            return;
        }

        List<String> vehicles = mapper.readValue(vehiclesJson, new TypeReference<List<String>>() {});

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


        RouteCreationDto routeCreationDto = new RouteCreationDto(companyId, cityId, transportModeId, routeNumber, vehicles);
        Result<Void> result = routeService.createRoute(routeCreationDto);
        if (!result.isSuccess()) {
            req.setAttribute("error", result.getErrorMessage());
            req.getRequestDispatcher("route_new.ftl").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/company/routes");

    }
}
