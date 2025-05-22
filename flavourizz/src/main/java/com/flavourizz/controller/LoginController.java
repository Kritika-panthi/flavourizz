package com.flavourizz.controller;

import java.io.IOException;

import com.flavourizz.model.UserModel;
import com.flavourizz.service.LoginService;
import com.flavourizz.util.CookieUtil;
import com.flavourizz.util.PasswordUtil;
import com.flavourizz.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    private String getRoleName(int roleId) {
        switch (roleId) {
            case 1: return "user";
            case 2: return "admin";
            case 3: return "chef";
            default: return "user";
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/loginpage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println("Login attempt for email: " + email);

        UserModel userModel = loginService.getUserByEmail(email);

        if (userModel == null) {
            System.out.println("User not found for email: " + email);
        } else {
            System.out.println("User found: " + userModel.getUsername());
            System.out.println("Stored password hash from DB: " + userModel.getPassword());

            boolean verified = PasswordUtil.verifyPassword(password, userModel.getPassword());
            System.out.println("Password verification result: " + verified);

            if (verified) {
            	req.getSession().setAttribute("currentUser", userModel);
                SessionUtil.setAttribute(req, "username", userModel.getUsername());
                String role = getRoleName(userModel.getRoleId());
                SessionUtil.setAttribute(req, "role", role);

                CookieUtil.addCookie(resp, "role", role, 1800);

                switch (role) {
                    case "admin":
                        resp.sendRedirect(req.getContextPath() + "/admin-dashboard");
                        break;
                    case "chef":
                        resp.sendRedirect(req.getContextPath() + "/chef/dashboard");
                        break;
                    case "user":
                    default:
                        resp.sendRedirect(req.getContextPath() + "/home");
                        break;
                }
                return;  // exit here since login succeeded
            }
        }

        // If user not found or password invalid, show error
        req.setAttribute("error", "Invalid email or password. Please try again!");
        req.getRequestDispatcher("/WEB-INF/pages/loginpage.jsp").forward(req, resp);
    }
}