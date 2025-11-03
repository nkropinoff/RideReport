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

@WebServlet("/profile/email")
public class ProfileChangeEmailServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Профиль");
        String newEmail = req.getParameter("newEmail");
        String currentEmail = ((UserSessionDto) req.getSession().getAttribute("user")).getEmail();
        req.setAttribute("currentEmail", currentEmail);

        if (newEmail == null || newEmail.isBlank()) {
            req.getSession().setAttribute("error", "Для обновления email поле не должно быть пустым.");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        UserSessionDto currentSession = (UserSessionDto) req.getSession(false).getAttribute("user");
        int userId = currentSession.getId();

        Result<UserSessionDto> result = userService.updateEmail(userId, newEmail);
        if (result.isSuccess()) {
            req.getSession().setAttribute("user", result.getData());
            req.getSession().setAttribute("successMessage", "Email успешно обновлен");
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            req.getSession().setAttribute("error", result.getErrorMessage());
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}
