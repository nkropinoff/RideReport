package ru.kpfu.itis.kropinov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kpfu.itis.kropinov.exceptions.AccessDeniedException;
import ru.kpfu.itis.kropinov.exceptions.DataAccessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/error-handler")
public class ExceptionHandlerServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerServlet.class);
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");

        if (throwable != null) {
            statusCode = getStatusCodeFromException(throwable);
        } else if (statusCode == null) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        if (throwable != null) {
            logger.error("Error occurred at URI: {}, Status: {}", requestUri, statusCode, throwable);
        } else {
            logger.error("Error occurred at URI: {}, Status: {}", requestUri, statusCode);
        }

        String requestedWith = req.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);
        boolean isApiRequest = requestUri != null && requestUri.contains("/api/");

        if (isAjax || isApiRequest) {
            handleApiError(resp, statusCode, throwable);
        } else {
            handlePageError(req, resp, statusCode, throwable, requestUri);
        }
    }

    private Integer getStatusCodeFromException(Throwable throwable) {
        if (throwable instanceof AccessDeniedException) {
            return 403;
        } else if (throwable instanceof DataAccessException) {
            return 500;
        } else {
            return 500;
        }
    }

    private void handleApiError(HttpServletResponse resp, int statusCode, Throwable throwable) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String errorMessage;
        if (statusCode >= 500) {
            errorMessage = "Произошла системная ошибка. Попробуйте позже.";
        } else if (statusCode == 404) {
            errorMessage = "Ресурс не найден";
        } else if (statusCode == 403) {
            errorMessage = "Доступ запрещен";
        } else if (statusCode == 400) {
            errorMessage = "Некорректный запрос";
        } else {
            errorMessage = "Произошла ошибка";
        }

        Map <String, Object> errorResponse = Map.of("error", errorMessage);
        mapper.writeValue(resp.getWriter(), errorResponse);
    }

    private void handlePageError(HttpServletRequest req, HttpServletResponse resp, int statusCode, Throwable throwable, String requestUri) throws ServletException, IOException {

        req.setAttribute("statusCode", statusCode);
        req.setAttribute("requestUri", requestUri == null ? "" : requestUri);

        String errorTitle;
        String errorDescription;

        switch (statusCode) {
            case 404:
                errorTitle = "Страница не найдена";
                errorDescription = "К сожалению, запрашиваемая страница не существует.";
                break;
            case 403:
                errorTitle = "Доступ запрещен";
                errorDescription = "У вас нет прав для доступа к этому ресурсу.";
                break;
            case 400:
                errorTitle = "Некорректный запрос";
                errorDescription = "Сервер не может обработать данный запрос.";
                break;
            case 500:
            default:
                errorTitle = "Произошла ошибка";
                errorDescription = "Извините, на сервере произошла ошибка. Мы уже работаем над её исправлением.";
                break;
        }

        req.setAttribute("errorTitle", errorTitle);
        req.setAttribute("errorDescription", errorDescription);
        req.setAttribute("pageTitle", errorTitle);

        req.getRequestDispatcher("error.ftl").forward(req, resp);
    }
}
