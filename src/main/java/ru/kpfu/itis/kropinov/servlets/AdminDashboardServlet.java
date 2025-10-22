package ru.kpfu.itis.kropinov.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Панель управления");
        req.setAttribute("sectionTitle", "Обзор");
        req.setAttribute("pageId", "overview");
        req.getRequestDispatcher("dashboard.ftl").forward(req, resp);
    }
}
