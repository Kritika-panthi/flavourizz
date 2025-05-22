package com.flavourizz.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.flavourizz.model.RecipeModel;
import com.flavourizz.service.CategoryService;
import com.flavourizz.service.RecipeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/category")
public class CategoryController extends HttpServlet {
    private CategoryService categoryService;
    private RecipeService recipeService;

    @Override
    public void init() {
        categoryService = new CategoryService();
        recipeService = new RecipeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String selectedCategoryId = request.getParameter("categoryId");
        List<RecipeModel> recipes = new ArrayList<>();

        if (selectedCategoryId != null && !selectedCategoryId.isEmpty()) {
            int catId = Integer.parseInt(selectedCategoryId);
            recipes = recipeService.getRecipesByCategoryId(catId);
            request.setAttribute("selectedCategoryId", catId);
        }

        request.setAttribute("recipes", recipes);
        request.setAttribute("categories", categoryService.getAllCategories());
        request.getRequestDispatcher("/WEB-INF/pages/category.jsp").forward(request, response);
    }
}
