package ru.kpfu.itis.kropinov.servlets;

import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.dto.UserSessionDto;
import ru.kpfu.itis.kropinov.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name="LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Вход в аккаунт");
        req.setAttribute("ctx", req.getContextPath());
        req.getRequestDispatcher("login.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("ctx", req.getContextPath());

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Поля 'Email' и 'Пароль' обязательны для заполнения.");
            req.getRequestDispatcher("login.ftl").forward(req, resp);
            return;
        }

        Result<UserSessionDto> result = userService.login(email, password);
        if (!result.isSuccess()) {
            req.setAttribute("error", result.getErrorMessage());
            req.getRequestDispatcher("login.ftl").forward(req, resp);
            return;
        }

        UserSessionDto userSessionDto = result.getData();
        HttpSession session = req.getSession();
        session.setAttribute("user", userSessionDto);

        resp.sendRedirect(generateRedirectUrl(userSessionDto, req.getContextPath()));
    }

    private String generateRedirectUrl(UserSessionDto userSessionDto, String contextPath) {
        return switch (userSessionDto.getRole()) {
            case ADMIN -> contextPath + "/admin/dashboard";
            case PASSENGER -> contextPath + "/";
            case COMPANY -> {
                Optional<UserSessionDto.CompanyInfo> companyInfoOptional = userSessionDto.getCompanyInfo();
                if (companyInfoOptional.isPresent()) {
                    yield switch (companyInfoOptional.get().getStatus()) {
                        case APPROVED -> contextPath + "/company/dashboard";
                        case PENDING -> contextPath + "/company/pending";
                        case DENIED -> contextPath + "/company/denied";
                    };
                }
                yield contextPath + "/";
            }
        };

    }
}
