package ru.kpfu.itis.kropinov.servlets.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/company/dashboard")
public class CompanyDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Панель управления");
        req.setAttribute("sectionTitle", "Статистика");
        req.setAttribute("pageId", "statistics");
        req.getRequestDispatcher("dashboard_company.ftl").forward(req, resp);
    }
}
