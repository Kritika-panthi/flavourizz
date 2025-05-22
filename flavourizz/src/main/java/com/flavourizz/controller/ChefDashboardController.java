package com.flavourizz.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

import com.flavourizz.model.RecipeModel;
import com.flavourizz.util.ChefUtil;
import com.flavourizz.service.ChefService; 

@WebServlet("/chef/dashboard")
public class ChefDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Retrieve chefId
        String email = ((com.flavourizz.model.UserModel) session.getAttribute("currentUser")).getEmail();
        int chefId = ChefUtil.getChefIdByEmail(email);

        // Fetch recipes
        ChefService chefService = new ChefService();
        List<RecipeModel> recipes = chefService.getRecipesByChefId(chefId);
        request.setAttribute("recipes", recipes);


        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/pages/chefdashboard.jsp").forward(request, response);
    }

}
