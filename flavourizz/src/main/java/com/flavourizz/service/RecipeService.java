package com.flavourizz.service;

import com.flavourizz.config.DbConfig;
import com.flavourizz.model.RecipeModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RecipeService {

    /**
     * Adds a new recipe to the database.
     *
     * @param recipe the RecipeModel containing all recipe data
     * @return true if the recipe was successfully inserted, false otherwise
     */
    public boolean addRecipe(RecipeModel recipe) {
        String sql = "INSERT INTO recipe (Chef_ID, Recipe_Title, Recipe_Description, Ingredients, Food_pic, Category_ID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Debug values before inserting
            System.out.println("Inserting Recipe:");
            System.out.println("Chef ID: " + recipe.getChefId());
            System.out.println("Title: " + recipe.getTitle());
            System.out.println("Description: " + recipe.getDescription());
            System.out.println("Ingredients: " + recipe.getIngredients());
            System.out.println("Food Pic: " + recipe.getFoodPic());
            System.out.println("Category ID: " + recipe.getCategoryId());

            ps.setInt(1, recipe.getChefId());
            ps.setString(2, recipe.getTitle());
            ps.setString(3, recipe.getDescription());
            ps.setString(4, recipe.getIngredients());
            ps.setString(5, recipe.getFoodPic());
            ps.setInt(6, recipe.getCategoryId());

            int rows = ps.executeUpdate();
            System.out.println("Rows inserted: " + rows);
            return rows > 0;

        } catch (Exception e) {
            System.out.println("Failed to insert recipe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
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
                recipe.setFoodPic(rs.getString("Food_Pic"));
                recipe.setCategoryId(rs.getInt("Category_ID"));
                list.add(recipe);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public boolean deleteRecipe(int recipeId) {
        System.out.println("Attempting to delete recipe with ID = " + recipeId);

        try (Connection conn = DbConfig.getDbConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            //  Delete ratings
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM rating WHERE Recipe_ID = ?")) {
                ps.setInt(1, recipeId);
                ps.executeUpdate();
            }

            // Delete from recipe_comment
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM recipe_comment WHERE Recipe_ID = ?")) {
                ps.setInt(1, recipeId);
                ps.executeUpdate();
            }

            //  delete  comments (no longer linked)
            try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM comment WHERE Comment_ID NOT IN (SELECT Comment_ID FROM recipe_comment)")) {
                ps.executeUpdate();
            }

            // Delete the recipe
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM recipe WHERE Recipe_ID = ?")) {
                ps.setInt(1, recipeId);
                int rows = ps.executeUpdate();
                System.out.println("Rows affected: " + rows);

                conn.commit();  // Commit the transaction
                return rows > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteChefAndRecipes(int userId) {
        try (Connection conn = DbConfig.getDbConnection()) {
            conn.setAutoCommit(false);

            // Step 1: Get Chef_ID from User_ID
            int chefId = -1;
            try (PreparedStatement ps = conn.prepareStatement("SELECT Chef_ID FROM chef WHERE User_ID = ?")) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    chefId = rs.getInt("Chef_ID");
                    System.out.println("Chef ID for deletion: " + chefId);
                } else {
                    System.out.println("Chef not found for user ID " + userId);
                    return false;
                }
            }

            // Step 2: Get all recipe IDs by this chef
            List<Integer> recipeIds = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT Recipe_ID FROM recipe WHERE Chef_ID = ?")) {
                ps.setInt(1, chefId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    recipeIds.add(rs.getInt("Recipe_ID"));
                }
            }

            // Step 3: Delete dependent data (ratings, recipe_comments, comments) for each recipe
            for (int recipeId : recipeIds) {
                try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM rating WHERE Recipe_ID = ?")) {
                    ps1.setInt(1, recipeId);
                    ps1.executeUpdate();
                }

                try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM recipe_comment WHERE Recipe_ID = ?")) {
                    ps2.setInt(1, recipeId);
                    ps2.executeUpdate();
                }

                try (PreparedStatement ps3 = conn.prepareStatement(
                        "DELETE FROM comment WHERE Comment_ID NOT IN (SELECT Comment_ID FROM recipe_comment)")) {
                    ps3.executeUpdate(); // Clean orphan comments
                }
            }

            // Step 4: Delete all recipes
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM recipe WHERE Chef_ID = ?")) {
                ps.setInt(1, chefId);
                int recipeCount = ps.executeUpdate();
                System.out.println("Deleted " + recipeCount + " recipes.");
            }

            // Step 5: Delete chef
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM chef WHERE Chef_ID = ?")) {
                ps.setInt(1, chefId);
                ps.executeUpdate();
                System.out.println("Deleted chef with ID: " + chefId);
            }

            // Step 6: Delete user
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE User_ID = ?")) {
                ps.setInt(1, userId);
                ps.executeUpdate();
                System.out.println("Deleted user with ID: " + userId);
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<RecipeModel> getAllRecipes() {
        List<RecipeModel> recipes = new ArrayList<>();
        String sql = "SELECT Recipe_ID, Recipe_Title, Recipe_Description, Food_pic FROM recipe";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            RatingService ratingService = new RatingService();

            while (rs.next()) {
                RecipeModel r = new RecipeModel();
                int recipeId = rs.getInt("Recipe_ID");

                r.setRecipeId(recipeId);
                r.setTitle(rs.getString("Recipe_Title"));
                r.setDescription(rs.getString("Recipe_Description"));
                r.setFoodPic(rs.getString("Food_pic"));

                // Fetch and set ratings
                r.setAverageRating(ratingService.getAverageRating(recipeId));
                r.setTotalRatings(ratingService.getTotalRatings(recipeId));

                recipes.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipes;
    }

        public List<RecipeModel> searchRecipes(String keyword) {
        	List<RecipeModel> list = new ArrayList<>();
            String sql = """
                SELECT r.Recipe_ID, r.Recipe_Title, r.Recipe_Description, r.Food_pic,
                       ROUND(AVG(rt.Score), 1) AS avgRating,
                       COUNT(rt.Score) AS totalRatings
                FROM recipe r
                LEFT JOIN rating rt ON r.Recipe_ID = rt.Recipe_ID
                WHERE r.Recipe_Title LIKE ? OR r.Recipe_Description LIKE ?
                GROUP BY r.Recipe_ID, r.Recipe_Title, r.Recipe_Description, r.Food_pic
            """;

            try (Connection conn = DbConfig.getDbConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                String likeKeyword = "%" + keyword + "%";
                ps.setString(1, likeKeyword);
                ps.setString(2, likeKeyword);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    RecipeModel r = new RecipeModel();
                    r.setRecipeId(rs.getInt("Recipe_ID"));
                    r.setTitle(rs.getString("Recipe_Title"));
                    r.setDescription(rs.getString("Recipe_Description"));
                    r.setFoodPic(rs.getString("Food_pic"));
                    r.setAverageRating(rs.getDouble("avgRating"));
                    r.setTotalRatings(rs.getInt("totalRatings"));

                    list.add(r);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return list;
        }
        public List<RecipeModel> getRecipesByCategoryId(int categoryId) {
            List<RecipeModel> list = new ArrayList<>();
            String sql = "SELECT Recipe_ID, Recipe_Title, Recipe_Description, Food_pic FROM recipe WHERE Category_ID = ?";

            try (Connection conn = DbConfig.getDbConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, categoryId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    RecipeModel r = new RecipeModel();
                    r.setRecipeId(rs.getInt("Recipe_ID"));
                    r.setTitle(rs.getString("Recipe_Title"));
                    r.setDescription(rs.getString("Recipe_Description"));
                    r.setFoodPic(rs.getString("Food_pic"));
                    list.add(r);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return list;
        }
        public List<RecipeModel> getTopRatedRecipes() {
            List<RecipeModel> list = new ArrayList<>();

            String sql = """
                SELECT r.Recipe_ID, r.Recipe_Title, r.Recipe_Description, r.Food_pic,
                       IFNULL(ROUND(AVG(rt.Score), 1), 0) AS averageRating,
                       COUNT(rt.Score) AS totalRatings
                FROM recipe r
                LEFT JOIN rating rt ON r.Recipe_ID = rt.Recipe_ID
                GROUP BY r.Recipe_ID, r.Recipe_Title, r.Recipe_Description, r.Food_pic
                ORDER BY averageRating DESC
                LIMIT 6
            """;

            try (Connection conn = DbConfig.getDbConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    RecipeModel r = new RecipeModel();
                    r.setRecipeId(rs.getInt("Recipe_ID"));
                    r.setTitle(rs.getString("Recipe_Title"));
                    r.setDescription(rs.getString("Recipe_Description"));
                    r.setFoodPic(rs.getString("Food_pic"));
                    r.setAverageRating(rs.getDouble("averageRating"));
                    r.setTotalRatings(rs.getInt("totalRatings"));
                    list.add(r);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return list;
        }

        public List<RecipeModel> getFirstThreeRecipes() {
            List<RecipeModel> recipes = new ArrayList<>();
            String sql = "SELECT Recipe_ID, Recipe_Title, Recipe_Description, Food_pic FROM recipe LIMIT 3";

            try (Connection conn = DbConfig.getDbConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                RatingService ratingService = new RatingService();

                while (rs.next()) {
                    RecipeModel r = new RecipeModel();
                    int recipeId = rs.getInt("Recipe_ID");

                    r.setRecipeId(recipeId);
                    r.setTitle(rs.getString("Recipe_Title"));
                    r.setDescription(rs.getString("Recipe_Description"));
                    r.setFoodPic(rs.getString("Food_pic"));

                    // Fetch and set ratings
                    r.setAverageRating(ratingService.getAverageRating(recipeId));
                    r.setTotalRatings(ratingService.getTotalRatings(recipeId));

                    recipes.add(r);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return recipes;
        }

}
