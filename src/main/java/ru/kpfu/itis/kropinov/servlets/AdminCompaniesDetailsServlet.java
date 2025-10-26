package ru.kpfu.itis.kropinov.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.CompanyDetailsDto;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.services.CompanyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/companies/*")
public class AdminCompaniesDetailsServlet extends HttpServlet {

    private final static Logger logger = LoggerFactory.getLogger(AdminCompaniesDetailsServlet.class);
    private CompanyService companyService;

    @Override
    public void init() throws ServletException {
        companyService = (CompanyService) getServletContext().getAttribute("companyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getPathInfo();
            if (path == null || path.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Company ID is required");
                return;
            }

            String[] parts = path.split("/");

            int companyId;
            try {
                companyId = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid company ID format");
                return;
            }

            Result<CompanyDetailsDto> result = companyService.getCompanyDetails(companyId);
            if (!result.isSuccess()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            req.setAttribute("pageTitle", "Информация о компании");
            req.setAttribute("company", result.getData());

            req.getRequestDispatcher("../company_details.ftl").forward(req, resp);

        } catch (Exception e) {
            logger.error("Failed doGet method", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
