package ru.kpfu.itis.kropinov.servlets.company;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.entities.Vehicle;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/api/company/routes/*")
public class CompanyRouteDetailsApiServlet extends HttpServlet {

    private RouteService routeService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Integer> routeIdOptional = extractRouteIdFromPath(req, resp);
        if (routeIdOptional.isEmpty()) {
            return;
        }

        int routeId = routeIdOptional.get();
        int companyId = getCompanyId(req);
        List<String> vehicles = routeService.getVehiclesByRouteId(routeId, companyId).stream().map(Vehicle::getNumber).toList();

        Map<String, Object> result = Map.of("vehicles", vehicles);
        resp.setContentType("application/json");
        mapper.writeValue(resp.getWriter(), result);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Integer> routeIdOptional = extractRouteIdFromPath(req, resp);
        if (routeIdOptional.isEmpty()) {
            return;
        }

        int routeId = routeIdOptional.get();
        int companyId = getCompanyId(req);

        List<String> vehicleNumbers = mapper.readValue(req.getReader(), new TypeReference<List<String>>() {});
        List<Vehicle> vehicles = vehicleNumbers.stream().map(Vehicle::new).toList();

        if (vehicles.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        routeService.updateRouteVehicles(routeId, vehicles, companyId);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Integer> routeIdOptional = extractRouteIdFromPath(req, resp);
        if (routeIdOptional.isEmpty()) {
            return;
        }

        int routeId = routeIdOptional.get();
        int companyId = getCompanyId(req);

        routeService.deleteRoute(routeId, companyId);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private int getCompanyId(HttpServletRequest req) {
        return ((UserSessionDto) req.getSession(false).getAttribute("user")).getCompanyInfo().get().getCompanyId();
    }

    private Optional<Integer> extractRouteIdFromPath(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return Optional.empty();
        }

        String[] parts = path.split("/");
        if (parts.length < 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return Optional.empty();
        }

        try {
            int routeId = Integer.parseInt(parts[1]);
            return Optional.of(routeId);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return Optional.empty();
        }
    }
}
