package ru.kpfu.itis.kropinov.servlets;

import ru.kpfu.itis.kropinov.dto.CompanyRegistrationDto;
import ru.kpfu.itis.kropinov.dto.PassengerRegistrationDto;
import ru.kpfu.itis.kropinov.dto.Result;
import ru.kpfu.itis.kropinov.entities.User;
import ru.kpfu.itis.kropinov.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 4 * (10 * 1024 * 1024)
)
@WebServlet(name="RegistrationServlet", urlPatterns = "/register")
public class RegistrationServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Регистрация");
        req.getRequestDispatcher("registration.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = req.getParameter("role");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        req.setAttribute("pageTitle", "Регистрация");
        req.setAttribute("role", role);
        req.setAttribute("email", email);

        if (role == null || !(role.equals("passenger") || role.equals("company"))) {
            req.setAttribute("error", "Роль регистрирующегося пользователя не указана или указана неверно.");
            req.getRequestDispatcher("registration.ftl").forward(req, resp);
            return;
        }

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Поля 'Email' и 'Пароль' обязательны для заполнения.");
            req.getRequestDispatcher("registration.ftl").forward(req, resp);
            return;
        }

        Result<Void> result;

        if (role.equals("passenger")) {
            PassengerRegistrationDto passengerRegistrationDto = new PassengerRegistrationDto(email, password);
            result = userService.registerPassenger(passengerRegistrationDto);
        } else {
            String companyName = req.getParameter("companyName");
            String inn = req.getParameter("inn");

            req.setAttribute("companyName", companyName);
            req.setAttribute("inn", inn);

            if (companyName == null || inn == null || companyName.isBlank() || inn.isBlank()) {
                req.setAttribute("error", "Поля 'Название компании' и 'ИНН' обязательны для заполнения.");
                req.getRequestDispatcher("registration.ftl").forward(req, resp);
                return;
            }

            List<Part> companyDocuments = req.getParts().stream()
                    .filter(part -> (part.getName().equals("documents") && part.getSize() > 0))
                    .toList();

            if (companyDocuments.size() > 4) {
                req.setAttribute("error", "Можно загрузить не более 4 документов");
                req.getRequestDispatcher("registration.ftl").forward(req, resp);
                return;
            }

            CompanyRegistrationDto companyRegistrationDto = new CompanyRegistrationDto(email, password, companyName, inn, companyDocuments);
            result = userService.registerCompany(companyRegistrationDto);
        }

        if (result.isSuccess()) {
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("error", result.getErrorMessage());
            req.getRequestDispatcher("registration.ftl").forward(req, resp);
        }
    }
}
