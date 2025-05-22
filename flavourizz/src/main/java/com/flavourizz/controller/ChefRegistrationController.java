package com.flavourizz.controller;

import java.io.IOException;

import com.flavourizz.model.UserModel;
import com.flavourizz.service.RegisterService;
import com.flavourizz.util.ImageUtil;
import com.flavourizz.util.PasswordUtil;
import com.flavourizz.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(asyncSupported = true, urlPatterns = { "/chefregister" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class ChefRegistrationController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RegisterService registerService;

    @Override
    public void init() {
        registerService = new RegisterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/chefregister.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Basic user fields
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String retypePassword = req.getParameter("retypePassword");
        String chefBio = req.getParameter("chefBio");
        String socialLinks = req.getParameter("socialLinks");

        // Normalize email
        String email = req.getParameter("email");
        if (email != null) {
            email = email.trim().toLowerCase();
        }
        // Validate required fields
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(email) ||
                ValidationUtil.isNullOrEmpty(password) || ValidationUtil.isNullOrEmpty(chefBio) ||
                !ValidationUtil.doPasswordsMatch(password, retypePassword)) {

            req.setAttribute("errorMessage", "Please fill all required fields correctly.");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.setAttribute("chefBio", chefBio);
            req.setAttribute("socialLinks", socialLinks);
            req.getRequestDispatcher("/WEB-INF/pages/chefregister.jsp").forward(req, resp);
            return;
        }

        // Encrypt password
        String encryptedPassword = PasswordUtil.hashPassword(password);
        // Role ID for chef = 3 (make sure your DB matches this)
        int roleId = 3;

        // Create UserModel object for user table insertion
        UserModel user = new UserModel(0, username, email, encryptedPassword, roleId);

        // Register user first (returns true if success)
        boolean userRegistered = registerService.registerUser(user);
        if (!userRegistered) {
            req.setAttribute("errorMessage", "An account with this email already exists.");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.setAttribute("chefBio", chefBio);
            req.setAttribute("socialLinks", socialLinks);
            req.getRequestDispatcher("/WEB-INF/pages/chefregister.jsp").forward(req, resp);
            return;
        }

        // Get the new user ID (assuming registerService provides it)
        int newUserId = registerService.getUserIdByEmail(email);
        if (newUserId <= 0) {
            // Rollback user if needed or show error
            req.setAttribute("errorMessage", "Error occurred. Please try again.");
            req.getRequestDispatcher("/WEB-INF/pages/chefregister.jsp").forward(req, resp);
            return;
        }

        // Handle profile pic upload
        Part profilePicPart = req.getPart("profilePic");
        String profilePicFileName = null;
        if (profilePicPart != null && profilePicPart.getSize() > 0) {
            // Save image and get filename
        	profilePicFileName = ImageUtil.saveImage(profilePicPart, req, "chefs");
        }

        // Save chef details in chef table
        boolean chefRegistered = registerService.registerChef(newUserId, chefBio, profilePicFileName, socialLinks);

        if (chefRegistered) {
            // Redirect or forward to login page on success
            req.getRequestDispatcher("/WEB-INF/pages/loginpage.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "Error saving chef details. Please try again.");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.setAttribute("chefBio", chefBio);
            req.setAttribute("socialLinks", socialLinks);
            req.getRequestDispatcher("/WEB-INF/pages/chefregister.jsp").forward(req, resp);
        }
    }
}