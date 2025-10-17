package ru.kpfu.itis.kropinov.servlets;

import ru.kpfu.itis.kropinov.dto.OperationResult;
import ru.kpfu.itis.kropinov.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="Registration Servlet", urlPatterns = "/register")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Регистрация");
        req.setAttribute("ctx", req.getContextPath());
        req.getRequestDispatcher("registration.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("ctx", req.getContextPath());

        String role = req.getParameter("role");
        if (role == null || !(role.equals("passenger") || role.equals("company"))) {
            req.setAttribute("error", "Роль регистрирующегося пользователя не указана или указана неверно.");
            req.getRequestDispatcher("registration.ftl").forward(req, resp);
            return;
        }

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Поля 'Email' и 'Пароль' обязательны для заполнения.");
            req.getRequestDispatcher("registration.ftl").forward(req, resp);
            return;
        }

        UserService userService = (UserService) getServletContext().getAttribute("userService");
        OperationResult result;

        if (role.equals("passenger")) {
            result = userService.registerPassenger(email, password);
        } else {
            String companyName = req.getParameter("companyName");
            String inn = req.getParameter("inn");

            if (companyName == null || inn == null || companyName.isBlank() || inn.isBlank()) {
                req.setAttribute("error", "Поля 'Название компании' и 'ИНН' обязательны для заполнения.");
                req.getRequestDispatcher("registration.ftl").forward(req, resp);
                return;
            }

            result = userService.registerCompany(email, password, companyName, inn);
        }

        if (result.isSuccess()) {
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("error", result.getErrorMessage());
            req.getRequestDispatcher("registration.ftl").forward(req, resp);
        }
    }
}
