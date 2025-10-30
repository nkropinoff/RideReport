package ru.kpfu.itis.kropinov.servlets.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.dto.ReviewSortingDto;
import ru.kpfu.itis.kropinov.dto.ReviewTableInfoDto;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.entities.Review;
import ru.kpfu.itis.kropinov.services.CompanyService;
import ru.kpfu.itis.kropinov.services.ReviewService;
import ru.kpfu.itis.kropinov.servlets.admin.AdminCompaniesApiServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/company/reviews")
public class CompanyReviewsApiServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CompanyReviewsApiServlet.class);

    private ReviewService reviewService;
    private ObjectMapper mapper;

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_ORDER = "desc";

    @Override
    public void init() throws ServletException {
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = parseIntOrDefault(req.getParameter("page"), DEFAULT_PAGE_NUMBER);
        String sortOrder = req.getParameter("sortOrder") != null ? req.getParameter("sortOrder") : DEFAULT_SORT_ORDER;

        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            sortOrder = DEFAULT_SORT_ORDER;
        }

        int companyId = ((UserSessionDto)  req.getSession(false).getAttribute("user")).getCompanyInfo().get().getCompanyId();

        ReviewSortingDto reviewSortingDto = new ReviewSortingDto(page, PAGE_SIZE, sortOrder, companyId);
        PaginatedResult<ReviewTableInfoDto> result = reviewService.getReviewsTableInfo(reviewSortingDto);

        resp.setContentType("application/json");
        mapper.writeValue(resp.getWriter(), result);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException e) {
            logger.debug("Invalid integer value: {}", value);
            return defaultValue;
        }
    }
}
