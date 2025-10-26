package ru.kpfu.itis.kropinov.servlets;

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

import static org.apache.commons.lang3.StringEscapeUtils.escapeJson;

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
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String parts[] = path.split("/");
        if (path.length() < 3) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing action or company ID");
            return;
        }

        int companyId;
        try {
            companyId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid company ID");
            return;
        }

        String action = parts[2];

        Result<Void> result;
        if (DENY.equals(action)) {
            result = companyService.denyCompany(companyId);
        } else if (APPROVE.equals(action)) {
            result = companyService.approveCompany(companyId);
        } else {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
            return;
        }

        if (result.isSuccess()) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            sendJsonError(resp, HttpServletResponse.SC_NOT_FOUND, result.getErrorMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Company ID required");
            return;
        }

        String[] parts = path.split("/");

        if (parts.length < 2) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path format");
            return;
        }

        int companyId;
        try {
            companyId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            sendJsonError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid company ID");
            return;
        }

        Result<Void> result = companyService.deleteCompany(companyId);
        if (result.isSuccess()) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            sendJsonError(resp, HttpServletResponse.SC_NOT_FOUND, result.getErrorMessage());
        }
    }

    private void sendJsonError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        writer.write("{\"error\":\"" + escapeJson(message) + "\"}");
        writer.flush();
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
