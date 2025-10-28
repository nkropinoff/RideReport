package ru.kpfu.itis.kropinov.filters;

import ru.kpfu.itis.kropinov.dto.UserSessionDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;

@WebFilter(filterName = "authorizationFilter")
public class AuthorizationFilter extends HttpFilter {

    private final Set<String> allowedPaths = Set.of("/", "/login", "/logout", "/register");
    private final String allowedStatic = "/assets";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String path = req.getRequestURI().substring(getServletContext().getContextPath().length());

        if (allowedPaths.contains(path) || path.startsWith(allowedStatic)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            res.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            res.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }

        boolean hasAccess = checkAccess(user, path);

        if (!hasAccess) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean checkAccess(UserSessionDto user, String path) {
        return switch(user.getRole()) {
            case ADMIN -> path.startsWith("/admin") || path.startsWith("/api/admin") || path.startsWith("/profile");
            case COMPANY -> path.startsWith("/company") || path.startsWith("/api/company") || path.startsWith("/profile");
            case PASSENGER -> path.startsWith("/review") || path.startsWith("/profile");
        };
    }
}
