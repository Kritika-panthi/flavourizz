package com.flavourizz.controller;

import com.flavourizz.model.RecipeModel;
import com.flavourizz.service.RecipeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/search")
public class SearchRecipeController extends HttpServlet {
    private RecipeService recipeService;

    @Override
    public void init() {
        recipeService = new RecipeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("q");
        List<RecipeModel> recipes;

        if (query != null && !query.trim().isEmpty()) {
            recipes = recipeService.searchRecipes(query.trim());
        } else {
            recipes = recipeService.getAllRecipes(); // fallback to full list
        }

        request.setAttribute("recipes", recipes);
        request.setAttribute("q", query); // to preserve search value
        request.getRequestDispatcher("/WEB-INF/pages/search.jsp").forward(request, response);
    }
    
}
