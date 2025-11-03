package ru.kpfu.itis.kropinov.servlets.company;

import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.entities.Vehicle;
import ru.kpfu.itis.kropinov.services.CompanyService;
import ru.kpfu.itis.kropinov.services.StatisticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/company/dashboard")
public class CompanyDashboardServlet extends HttpServlet {
    private StatisticsService statisticsService;
    private CompanyService companyService;

    @Override
    public void init() throws ServletException {
        statisticsService = (StatisticsService) getServletContext().getAttribute("statisticsService");
        companyService = (CompanyService) getServletContext().getAttribute("companyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Панель управления");
        req.setAttribute("sectionTitle", "Статистика");
        req.setAttribute("pageId", "statistics");

        int companyId = ((UserSessionDto) req.getSession(false).getAttribute("user")).getCompanyInfo().get().getCompanyId();
        req.setAttribute("companyStats", statisticsService.getCompanyStatistics(companyId));

        List<String> vehicleNumbers = companyService.getCompanyVehicles(companyId).stream().map(Vehicle::getNumber).toList();
        req.setAttribute("companyVehicleNumbers", vehicleNumbers);

        req.getRequestDispatcher("dashboard_company.ftl").forward(req, resp);
    }
}
