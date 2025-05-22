package com.flavourizz.controller;

import java.io.IOException;
import java.util.Map;

import com.flavourizz.model.UserModel;
import com.flavourizz.service.RegisterService;
import com.flavourizz.util.PasswordUtil;
import com.flavourizz.util.ImageUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@SuppressWarnings("serial")
@WebServlet("/editChefProfile")
@MultipartConfig
public class EditChefProfileController extends HttpServlet {
    private RegisterService registerService;

    @Override
    public void init() {
        registerService = new RegisterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = (UserModel) req.getSession().getAttribute("currentUser");
        if (user == null) {
        	resp.sendRedirect(req.getContextPath() + "/login");
        	return;
        }
        Map<String, String> chefDetails = registerService.getChefDetails(user.getUserId());

        req.setAttribute("chefBio", chefDetails.get("bio"));
        req.setAttribute("socialLinks", chefDetails.get("links"));
        req.getRequestDispatcher("/WEB-INF/pages/editchefprofile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = (UserModel) req.getSession().getAttribute("currentUser");

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String bio = req.getParameter("chefBio");
        String links = req.getParameter("socialLinks");

        // Optional password update
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmNewPassword");

        if (newPassword != null && !newPassword.isBlank()) {
            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("errorMessage", "New passwords do not match.");
                doGet(req, resp);
                return;
            }

            if (!PasswordUtil.verifyPassword(currentPassword, user.getPassword())) {
                req.setAttribute("errorMessage", "Current password is incorrect.");
                doGet(req, resp);
                return;
            }

            String encrypted = PasswordUtil.hashPassword(newPassword);
            registerService.updateUserPassword(user.getUserId(), encrypted);
            user.setPassword(encrypted);
        }

        // Profile pic
        Part picPart = req.getPart("profilePic");
        String fileName = null;
        if (picPart != null && picPart.getSize() > 0) {
        	fileName = ImageUtil.saveImage(picPart, req, "chefs");
        }

        user.setUsername(username);
        user.setEmail(email);
        registerService.updateUser(user);
        registerService.updateChef(user.getUserId(), bio, fileName, links);

        req.getSession().setAttribute("currentUser", user);
        resp.sendRedirect(req.getContextPath() + "/chef/dashboard");
    }
}