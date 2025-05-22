package com.flavourizz.controller;

import java.io.IOException;
import java.util.List;

import com.flavourizz.model.RecipeModel;
import com.flavourizz.model.UserModel;
import com.flavourizz.service.RecipeService;
import com.flavourizz.util.ChefUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/myrecipes") 
public class ChefRecipeListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RecipeService recipeService;

    @Override
    public void init() {
        recipeService = new RecipeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserModel user = (UserModel) req.getSession().getAttribute("currentUser");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int chefId = ChefUtil.getChefIdByEmail(user.getEmail());
        List<RecipeModel> recipes = recipeService.getRecipesByChefId(chefId);

        req.setAttribute("recipes", recipes);
        req.getRequestDispatcher("/WEB-INF/pages/myrecipes.jsp").forward(req, resp);
    }
}
