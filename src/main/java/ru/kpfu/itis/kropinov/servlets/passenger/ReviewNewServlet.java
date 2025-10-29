package ru.kpfu.itis.kropinov.servlets.passenger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.services.ReviewService;
import ru.kpfu.itis.kropinov.services.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reviews/new")
public class ReviewNewServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(ReviewNewServlet.class);
    private RouteService routeService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        routeService = (RouteService) getServletContext().getAttribute("routeService");
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Оставить отзыв");
        req.setAttribute("cities", routeService.getAllCities());
        req.setAttribute("transportModes", routeService.getAllTransportModes());
        req.setAttribute("feedbackCategories", reviewService.getAllFeedbackCategoriesWithTags());

        req.getRequestDispatcher("../review_new.ftl").forward(req, resp);
    }
}
