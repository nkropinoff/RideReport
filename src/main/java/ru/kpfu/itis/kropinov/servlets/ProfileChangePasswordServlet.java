package ru.kpfu.itis.kropinov.servlets;

import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile/password")
public class ProfileChangePasswordServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Профиль");
        String currentEmail = ((UserSessionDto) req.getSession().getAttribute("user")).getEmail();
        req.setAttribute("currentEmail", currentEmail);

        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (currentPassword == null || currentPassword.isBlank() ||
            confirmPassword == null || confirmPassword.isBlank() ||
            newPassword == null || newPassword.isBlank()) {
            req.getSession().setAttribute("error", "Поля 'Текущий пароль', 'Новый пароль' и 'Подтверждение нового пароля' не должны быть пустыми");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        UserSessionDto currentSession = (UserSessionDto) req.getSession(false).getAttribute("user");
        int userId = currentSession.getId();

        Result<Void> result = userService.updatePassword(userId, currentPassword, newPassword, confirmPassword);
        if (result.isSuccess()) {
            req.getSession().setAttribute("successMessage", "Пароль успешно обновлен");
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            req.getSession().setAttribute("error", result.getErrorMessage());
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}
