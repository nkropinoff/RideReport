package ru.kpfu.itis.kropinov.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthenticationFilter extends HttpFilter {

    private final Set<String> allowedPaths = Set.of("/", "/login", "/register");
    private final String allowedStatic = "/assets";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith(allowedStatic) || allowedPaths.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(getServletContext().getContextPath() + "/login");
        } else {
            chain.doFilter(req, res);
        }
    }
}
