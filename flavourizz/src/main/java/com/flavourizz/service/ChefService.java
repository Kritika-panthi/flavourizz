package com.flavourizz.service;

import com.flavourizz.config.DbConfig;
import com.flavourizz.model.RecipeModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChefService {

    public List<RecipeModel> getRecipesByChefId(int chefId) {
        List<RecipeModel> list = new ArrayList<>();
        String sql = "SELECT * FROM Recipe WHERE Chef_ID = ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, chefId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RecipeModel recipe = new RecipeModel();
                recipe.setRecipeId(rs.getInt("Recipe_ID"));
                recipe.setChefId(rs.getInt("Chef_ID"));
                recipe.setTitle(rs.getString("Recipe_Title"));
                recipe.setDescription(rs.getString("Recipe_Description"));
                recipe.setIngredients(rs.getString("Ingredients"));
                recipe.setFoodPic(rs.getString("Food_pic"));
                recipe.setCategoryId(rs.getInt("Category_ID"));
                list.add(recipe);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
