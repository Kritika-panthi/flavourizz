package com.flavourizz.controller;

import java.io.IOException;

import com.flavourizz.util.CookieUtil;
import com.flavourizz.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Invalidate session
        SessionUtil.invalidate(req);

        // Remove role cookie by setting max age to 0
        CookieUtil.deleteCookie(resp, "role");

        // Redirect to login page after logout
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
