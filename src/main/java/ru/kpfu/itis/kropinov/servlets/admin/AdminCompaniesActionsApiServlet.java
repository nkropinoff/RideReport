package ru.kpfu.itis.kropinov.servlets.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.services.CompanyService;
import ru.kpfu.itis.kropinov.services.impl.CompanyServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/admin/companies/*")
public class AdminCompaniesActionsApiServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AdminCompaniesActionsApiServlet.class);
    private CompanyService companyService;

    private final static String APPROVE = "approve";
    private final static String DENY = "deny";

    @Override
    public void init() throws ServletException {
        companyService = (CompanyServiceImpl) getServletContext().getAttribute("companyService");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String parts[] = path.split("/");
        if (path.length() < 3) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int companyId;
        try {
            companyId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String action = parts[2];

        Result<Void> result;
        if (DENY.equals(action)) {
            result = companyService.denyCompany(companyId);
        } else if (APPROVE.equals(action)) {
            result = companyService.approveCompany(companyId);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (result.isSuccess()) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        int companyId;
        try {
            companyId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Result<Void> result = companyService.deleteCompany(companyId);
        if (result.isSuccess()) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
