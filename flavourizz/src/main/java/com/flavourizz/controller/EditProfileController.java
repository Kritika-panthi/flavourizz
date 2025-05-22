package com.flavourizz.controller;

import com.flavourizz.model.UserModel;
import com.flavourizz.service.UserService;
import com.flavourizz.util.PasswordUtil;
import com.flavourizz.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/user/edit-profile")
public class EditProfileController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = (String) SessionUtil.getAttribute(req, "username");
        if (username == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserModel user = userService.getUserByUsername(username);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/pages/editprofile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newUsername = req.getParameter("username");
        String newPassword = req.getParameter("password");

        String currentUsername = (String) SessionUtil.getAttribute(req, "username");
        UserModel user = userService.getUserByUsername(currentUsername);

        if (user != null && newUsername != null && !newUsername.isBlank()) {
            user.setUsername(newUsername);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            user.setPassword(hashedPassword);
        }

        userService.updateUser(user);
        SessionUtil.setAttribute(req, "username", newUsername); // update session
        req.getSession().setAttribute("successMessage", "Username and password updated successfully.");
       
        resp.sendRedirect(req.getContextPath() + "/user/edit-profile");
    }
}