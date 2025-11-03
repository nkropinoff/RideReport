package ru.kpfu.itis.kropinov.servlets.admin;

import ru.kpfu.itis.kropinov.services.ReviewService;
import ru.kpfu.itis.kropinov.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private UserService userService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Панель управления");
        req.setAttribute("sectionTitle", "Обзор");
        req.setAttribute("pageId", "overview");

        req.setAttribute("countCompanies", userService.countAllCompanies());
        req.setAttribute("countPassengers", userService.countAllPassengers());
        req.setAttribute("countReviews", reviewService.countAllReviews());

        req.getRequestDispatcher("dashboard_admin.ftl").forward(req, resp);
    }
}
