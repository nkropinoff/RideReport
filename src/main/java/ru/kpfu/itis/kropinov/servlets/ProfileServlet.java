package ru.kpfu.itis.kropinov.servlets;

import ru.kpfu.itis.kropinov.dto.UserSessionDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Профиль");
        String currentEmail = ((UserSessionDto) req.getSession().getAttribute("user")).getEmail();
        req.setAttribute("currentEmail", currentEmail);

        String error = (String) req.getSession().getAttribute("error");
        if (error != null) {
            req.setAttribute("error", error);
            req.getSession().removeAttribute("error");
        }

        String successMessage = (String) req.getSession().getAttribute("successMessage");
        if (successMessage != null) {
            req.setAttribute("successMessage", successMessage);
            req.getSession().removeAttribute("successMessage");
        }

        req.getRequestDispatcher("profile.ftl").forward(req, resp);
    }
}
