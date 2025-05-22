package com.flavourizz.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/contact")
public class ContactUsController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Forward to the contact.jsp page
        req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, resp);
    }
}
