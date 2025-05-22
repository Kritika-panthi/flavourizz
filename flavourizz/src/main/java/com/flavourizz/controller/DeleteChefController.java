package com.flavourizz.controller;

import java.io.IOException;

import com.flavourizz.service.RecipeService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/deleteChef")
public class DeleteChefController extends HttpServlet {
    private RecipeService recipeService;

    @Override
    public void init() {
        recipeService = new RecipeService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            int userId = ((com.flavourizz.model.UserModel) session.getAttribute("currentUser")).getUserId();
            boolean deleted = recipeService.deleteChefAndRecipes(userId);  
            if (deleted) {
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                resp.sendRedirect(req.getContextPath() + "/chef/dashboard?error=deleteFailed");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}