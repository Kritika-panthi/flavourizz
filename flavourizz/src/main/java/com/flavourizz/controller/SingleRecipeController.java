package com.flavourizz.controller;

import com.flavourizz.service.CommentService;
import com.flavourizz.service.RatingService;
import com.flavourizz.model.Comment;
import com.flavourizz.config.DbConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/singlerecipe")
public class SingleRecipeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int recipeId = 0;
        String recipeIdStr = request.getParameter("recipeId");

        if (recipeIdStr != null) {
            try {
                recipeId = Integer.parseInt(recipeIdStr);
            } catch (NumberFormatException e) {
                recipeId = 0;
            }
        }

        if (recipeId == 0) {
            try (Connection conn = DbConfig.getDbConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT Recipe_ID FROM recipe LIMIT 1");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    recipeId = rs.getInt("Recipe_ID");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (recipeId == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Recipe not found");
            return;
        }

        // Fields to retrieve
        String title = "", description = "", ingredients = "", foodPic = "", categoryName = "", chefName = "";

        // Fetch recipe details with proper JOINs
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT r.*, c.Category_Name, u.Username " +
                 "FROM recipe r " +
                 "JOIN category c ON r.Category_ID = c.Category_ID " +
                 "JOIN chef ch ON r.Chef_ID = ch.Chef_ID " +
                 "JOIN users u ON ch.User_ID = u.User_ID " +
                 "WHERE r.Recipe_ID = ?")) {

            stmt.setInt(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    title = rs.getString("Recipe_Title");
                    description = rs.getString("Recipe_Description");
                    ingredients = rs.getString("Ingredients");
                    foodPic = rs.getString("Food_pic");
                    categoryName = rs.getString("Category_Name");
                    chefName = rs.getString("Username");
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Recipe not found");
                    return;
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // Load comments
        CommentService commentService = new CommentService();
        List<Comment> comments = commentService.getCommentsByRecipeId(recipeId);
        request.setAttribute("comments", comments);

        // Load rating data
        RatingService ratingService = new RatingService();
        double averageRating = ratingService.getAverageRating(recipeId);
        int totalRatings = ratingService.getTotalRatings(recipeId);

        // Set attributes for JSP
        request.setAttribute("recipeId", recipeId);
        request.setAttribute("title", title);
        request.setAttribute("description", description);
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("foodPic", foodPic);
        request.setAttribute("categoryName", categoryName);
        request.setAttribute("chefName", chefName);
        request.setAttribute("averageRating", averageRating);
        request.setAttribute("totalRatings", totalRatings);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/pages/singlerecipe.jsp").forward(request, response);
    }
}