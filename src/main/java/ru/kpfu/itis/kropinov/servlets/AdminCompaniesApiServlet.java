package ru.kpfu.itis.kropinov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.CompanySortingDto;
import ru.kpfu.itis.kropinov.dto.PaginatedResult;
import ru.kpfu.itis.kropinov.entities.Company;
import ru.kpfu.itis.kropinov.enums.VerifyStatus;
import ru.kpfu.itis.kropinov.services.CompanyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/admin/companies")
public class AdminCompaniesApiServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AdminCompaniesApiServlet.class);

    private CompanyService companyService;
    private ObjectMapper mapper;

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_ORDER = "desc";

    @Override
    public void init() throws ServletException {
        companyService = (CompanyService) getServletContext().getAttribute("companyService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            int page = parseIntOrDefault(req.getParameter("page"), DEFAULT_PAGE_NUMBER);
            String sortOrder = req.getParameter("sortOrder") != null ? req.getParameter("sortOrder") : DEFAULT_SORT_ORDER;
            VerifyStatus status = parseStatus(req.getParameter("status"));

            if (page < 1) page = DEFAULT_PAGE_NUMBER;

            if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
                sortOrder = DEFAULT_SORT_ORDER;
            }

            CompanySortingDto companySortingDto = new CompanySortingDto(page, PAGE_SIZE, sortOrder, status);
            PaginatedResult<Company> companies = companyService.getCompanies(companySortingDto);

            mapper.writeValue(resp.getWriter(), companies);

        } catch (Exception e) {
            logger.error("Failed to fetch companies", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Internal server error\"}");
        }
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

    private VerifyStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return VerifyStatus.valueOf(status);
        } catch (IllegalStateException e) {
            logger.debug("Invalid status value: {}", status);
            return null;
        }

    }
}
