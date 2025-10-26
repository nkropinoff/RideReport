package ru.kpfu.itis.kropinov.servlets.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.dto.FileDownloadDto;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.services.CompanyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/admin/companies/download/*")
public class AdminCompaniesDownloadServlet extends HttpServlet {
    private CompanyService companyService;
    private final static Logger logger = LoggerFactory.getLogger(AdminCompaniesDownloadServlet.class);

    @Override
    public void init() throws ServletException {
        companyService = (CompanyService) getServletContext().getAttribute("companyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getPathInfo().substring(1));

            Result<FileDownloadDto> result = companyService.getFileForDownload(id);
            if (!result.isSuccess()) {
                logger.error(("Failed to get file for download with id: {}"), id);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            FileDownloadDto file = result.getData();

            resp.setContentType(file.getMimeType());
            String encoded = URLEncoder.encode(file.getFilename(), StandardCharsets.UTF_8).replace("+", "%20");
            resp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encoded);

            try (InputStream in = new URL(file.getUrl()).openStream();
                 OutputStream out = resp.getOutputStream()) {
                in.transferTo(out);
            }

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
