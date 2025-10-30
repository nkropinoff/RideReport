package ru.kpfu.itis.kropinov.servlets.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/company/reviews")
public class CompanyReviewsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Отзывы");
        req.setAttribute("sectionTitle", "Отзывы");
        req.setAttribute("pageId", "reviews");

        req.getRequestDispatcher("reviews_company.ftl").forward(req, resp);
    }
}
