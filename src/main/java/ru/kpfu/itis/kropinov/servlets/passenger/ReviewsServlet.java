package ru.kpfu.itis.kropinov.servlets.passenger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.ReviewCreationDto;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.services.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@MultipartConfig(
    maxFileSize = 10 * 1024 * 1024,
    maxRequestSize = 10 * 1024 * 1024
)
@WebServlet("/reviews")
public class ReviewsServlet extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(ReviewsServlet.class);
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String routeIdString = req.getParameter("routeId");
        String vehicleNumber = req.getParameter("vehicleNumber");
        String rideTimeString = req.getParameter("rideTime");
        String selectedTags = req.getParameter("selectedTags");
        String reviewText = req.getParameter("reviewText");
        String provideEmailString = req.getParameter("provideEmail");

        Part photo;
        try {
            photo = req.getPart("photo");
        } catch (IllegalStateException e) {
            logger.error("Failed to get file part", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int routeId;
        try { routeId = Integer.parseInt(routeIdString); }
        catch (NumberFormatException e) {
            logger.error("Failed parse route id", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            logger.error("Vehicle number is null or blank");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (rideTimeString == null || rideTimeString.isBlank()) {
            logger.error("rite time is null or blank");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (selectedTags == null || selectedTags.isBlank()) {
            logger.error("selected tags is null or blank");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<Integer> feedbackTagIds = new ArrayList<>();
        try {
            feedbackTagIds = Arrays.stream(selectedTags.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
        } catch (NumberFormatException e) {
            logger.error("Failed to parse selected tags, received: {}", selectedTags);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        reviewText = (reviewText == null || reviewText.trim().isEmpty()) ? null : reviewText.trim();
        LocalDateTime rideTime = LocalDateTime.parse(rideTimeString);
        boolean provideEmail = "true".equals(provideEmailString);

        int userId = ((UserSessionDto) req.getSession(false).getAttribute("user")).getId();

        ReviewCreationDto dto = new ReviewCreationDto(userId, routeId, vehicleNumber, rideTime, feedbackTagIds, reviewText, provideEmail, photo);
        reviewService.createReview(dto);

        resp.sendRedirect(req.getContextPath());
    }
}
