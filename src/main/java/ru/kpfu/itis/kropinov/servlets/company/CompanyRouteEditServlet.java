package ru.kpfu.itis.kropinov.servlets.company;

import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/company/routes/edit")
public class CompanyRouteEditServlet extends HttpServlet {
    private RouteService routeService;

    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Редактор маршрута");
        req.setAttribute("cities", routeService.getAllCities());
        req.setAttribute("transportModes", routeService.getAllTransportModes());

        req.getRequestDispatcher("../route_edit.ftl").forward(req, resp);
    }
}
