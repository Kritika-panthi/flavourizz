package com.flavourizz.controller;

import java.io.IOException;

import com.flavourizz.model.UserModel;
import com.flavourizz.service.RegisterService;
import com.flavourizz.util.PasswordUtil;
import com.flavourizz.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class RegistrationController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RegisterService registerService;

    @Override
    public void init() {
        registerService = new RegisterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/registrationpage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String retypePassword = req.getParameter("retypePassword");

        // Normalize email
        String email = req.getParameter("email");
        if (email != null) {
            email = email.trim().toLowerCase();  // ✅ reassign existing variable
        }
        // Role is hardcoded as 1 (user)
        int roleId = 1;

        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(email) ||
                ValidationUtil.isNullOrEmpty(password) ||
                !ValidationUtil.doPasswordsMatch(password, retypePassword)) {

            req.setAttribute("errorMessage", "Please fill all fields correctly.");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            // No role attribute needed anymore
            req.getRequestDispatcher("/WEB-INF/pages/registrationpage.jsp").forward(req, resp);
            return;
        }

        // Encrypt password
        String encryptedPassword = PasswordUtil.hashPassword(password);

        UserModel user = new UserModel(0, username, email, encryptedPassword, roleId);

        boolean success = registerService.registerUser(user);

        if (success) {
            req.getRequestDispatcher("/WEB-INF/pages/loginpage.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "An account with this email already exists.");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/pages/registrationpage.jsp").forward(req, resp);
        }
    }
}
