package ru.kpfu.itis.kropinov.servlets.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.kropinov.dto.CategoryStatDto;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.services.StatisticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/company/statistics/vehicle")
public class CompanyVehicleStatisticsApiServlet extends HttpServlet {

    private StatisticsService statisticsService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        statisticsService = (StatisticsService) getServletContext().getAttribute("statisticsService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String vehicleNumber = req.getParameter("number");
        int companyId = ((UserSessionDto) req.getSession().getAttribute("user")).getCompanyInfo().get().getCompanyId();

        List<CategoryStatDto> stats = statisticsService.getVehicleNumberStatistics(companyId, vehicleNumber);
        mapper.writeValue(resp.getWriter(), stats);
    }
}
