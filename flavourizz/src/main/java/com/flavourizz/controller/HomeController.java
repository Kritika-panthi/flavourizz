package com.flavourizz.controller;

import com.flavourizz.model.RecipeModel;
import com.flavourizz.service.RecipeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RecipeService recipeService;

    @Override
    public void init() {
        recipeService = new RecipeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<RecipeModel> trendingRecipes = recipeService.getTopRatedRecipes();
        List<RecipeModel> firstThreeRecipes = recipeService.getFirstThreeRecipes();
        req.setAttribute("trendingRecipes", trendingRecipes);
        req.setAttribute("randomRecipes", firstThreeRecipes);
        req.getRequestDispatcher("/WEB-INF/pages/homepage.jsp").forward(req, resp);
    }
}
	
