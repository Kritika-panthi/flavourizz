package com.flavourizz.controller;

import com.flavourizz.model.Rating;
import com.flavourizz.service.RatingService;
import com.flavourizz.config.DbConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/rate")
public class RateRecipeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String recipeIdStr = request.getParameter("recipeId");
        String scoreStr = request.getParameter("score");

        HttpSession session = request.getSession();

        if (username == null || recipeIdStr == null || scoreStr == null) {
            session.setAttribute("message", "Invalid rating submission.");
            response.sendRedirect(request.getContextPath() + "/WEB-INF/pages/singlerecipe.jsp?recipeId=" + recipeIdStr);
            return;
        }

        int userId = -1;
        int recipeId = 0;
        int score = 0;

        try {
            recipeId = Integer.parseInt(recipeIdStr);
            score = Integer.parseInt(scoreStr);
        } catch (NumberFormatException e) {
            session.setAttribute("message", "Invalid rating values.");
            response.sendRedirect(request.getContextPath() + "/WEB-INF/pages/singlerecipe.jsp?recipeId=" + recipeIdStr);
            return;
        }

        // Get userId from username
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT User_ID FROM users WHERE username = ?")) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("User_ID");
                } else {
                    session.setAttribute("message", "User not found.");
                    response.sendRedirect(request.getContextPath() + "/WEB-INF/pages/singlerecipe.jsp?recipeId=" + recipeId);
                    return;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            session.setAttribute("message", "Database error occurred.");
            response.sendRedirect(request.getContextPath() + "/WEB-INF/pages/singlerecipe.jsp?recipeId=" + recipeId);
            return;
        }

        Rating rating = new Rating(userId, recipeId, score);
        RatingService service = new RatingService();

        boolean success = service.submitRating(rating);

        if (success) {
            session.setAttribute("message", "Rating submitted successfully!");
        } else {
            session.setAttribute("message", "Failed to submit rating.");
        }

        response.sendRedirect(request.getContextPath() + "/singlerecipe?recipeId=" + recipeId);

    }
}
