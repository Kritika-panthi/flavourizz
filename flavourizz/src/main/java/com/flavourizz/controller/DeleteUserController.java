package com.flavourizz.controller;

import com.flavourizz.service.RecipeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/delete-user")
public class DeleteUserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RecipeService recipeService;

    @Override
    public void init() {
        recipeService = new RecipeService(); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");

        if (userIdParam != null) {
            int userId = Integer.parseInt(userIdParam);
            boolean deleted = recipeService.deleteChefAndRecipes(userId); 

            HttpSession session = request.getSession();
            if (deleted) {
                session.setAttribute("message", "User and all associated data deleted successfully.");
            } else {
                session.setAttribute("message", "Failed to delete user and data.");
            }

            response.sendRedirect(request.getContextPath() + "/admin-dashboard?tab=usersTab");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard?tab=usersTab&error=missingId");
        }
    }
}
