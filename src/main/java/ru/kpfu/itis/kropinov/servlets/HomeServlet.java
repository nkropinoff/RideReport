package ru.kpfu.itis.kropinov.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="HomeServlet", urlPatterns = "/index")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("ctx", req.getContextPath());
        req.getRequestDispatcher("/WEB-INF/templates/index.ftl").forward(req, resp);
    }
}
