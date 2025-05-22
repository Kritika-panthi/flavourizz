package com.flavourizz.controller;

import java.io.IOException;

import com.flavourizz.model.RecipeModel;
import com.flavourizz.model.UserModel;
import com.flavourizz.service.RecipeService;
import com.flavourizz.service.CategoryService;
import com.flavourizz.util.ChefUtil;
import com.flavourizz.util.ImageUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(urlPatterns = {"/addrecipe", "/deleterecipe"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)
public class RecipeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RecipeService recipeService;

    @Override
    public void init() {
        recipeService = new RecipeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("categories", new CategoryService().listAll());
        req.getRequestDispatcher("/WEB-INF/pages/addrecipepage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/deleterecipe".equals(path)) {
            handleDeleteRecipe(req, resp);
        } else {
            handleAddRecipe(req, resp);
        }
    }

    private void handleAddRecipe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel currentUser = (UserModel) req.getSession().getAttribute("currentUser");

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String email = currentUser.getEmail();
        int chefId = ChefUtil.getChefIdByEmail(email);
        if (chefId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/addrecipe?error=invalidchef");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String ingredients = req.getParameter("ingredients");
        String categoryIdStr = req.getParameter("categoryId");

        if (title == null || description == null || ingredients == null || categoryIdStr == null ||
                title.isEmpty() || description.isEmpty() || ingredients.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/addrecipe?error=save");
            return;
        }

        int categoryId = Integer.parseInt(categoryIdStr);

        String foodPicFileName = null;
        Part foodPicPart = req.getPart("foodPic");

        if (foodPicPart != null && foodPicPart.getSize() > 0) {
            foodPicFileName = ImageUtil.saveImage(foodPicPart, req, "recipes");
        }

        RecipeModel recipe = new RecipeModel();
        recipe.setChefId(chefId);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setIngredients(ingredients);
        recipe.setFoodPic(foodPicFileName);
        recipe.setCategoryId(categoryId);

        boolean saved = recipeService.addRecipe(recipe);

        if (saved) {
            resp.sendRedirect(req.getContextPath() + "/chef/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/addrecipe?error=save");
        }
    }

    private void handleDeleteRecipe(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String recipeIdStr = req.getParameter("id");

        if (recipeIdStr == null || recipeIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/myrecipes?error=invalidid");
            return;
        }

        int recipeId = Integer.parseInt(recipeIdStr);
        boolean deleted = recipeService.deleteRecipe(recipeId);

        if (deleted) {
            resp.sendRedirect(req.getContextPath() + "/myrecipes");
        } else {
            resp.sendRedirect(req.getContextPath() + "/myrecipes?error=deletefailed");
        }
    }
}

