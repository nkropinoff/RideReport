package ru.kpfu.itis.kropinov.servlets.company;

import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.ReviewDetailsDto;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.services.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/company/reviews/*")
public class CompanyReviewsDetailsServlet extends HttpServlet {
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] parts = path.split("/");
        if (parts.length < 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int reviewId;
        try {
            reviewId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int companyId = ((UserSessionDto) req.getSession().getAttribute("user")).getCompanyInfo().get().getCompanyId();
        Result<ReviewDetailsDto> result = reviewService.getReviewDetails(reviewId, companyId);

        if (!result.isSuccess()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("pageTitle", "Подробности отзыва");
        req.setAttribute("review", result.getData());
        req.getRequestDispatcher("../review_details.ftl").forward(req, resp);
    }
}
