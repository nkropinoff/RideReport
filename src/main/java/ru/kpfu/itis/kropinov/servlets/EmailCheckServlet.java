package ru.kpfu.itis.kropinov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.kropinov.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name="EmailCheckServlet", urlPatterns = "/api/check-email")
public class EmailCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");

        boolean isAvailable = false;
        if (email != null && !email.isBlank()) {
            isAvailable = !((UserService) getServletContext().getAttribute("userService")).isEmailTaken(email);
        }

        Map<String, Boolean> responseData = Map.of("isAvailable", isAvailable);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        mapper.writeValue(resp.getWriter(), responseData);
    }
}
