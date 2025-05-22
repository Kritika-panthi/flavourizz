package com.flavourizz.model;

public class RecipeModel {
    private int recipeId;
    private int chefId;
    private String title;
    private String description;
    private String ingredients;
    private String foodPic;
    private int categoryId;
    private double averageRating;
    private int totalRatings;

    public RecipeModel(int recipeId, int chefId, String title,
                       String description, String ingredients,
                       String foodPic, int categoryId) {
        this.recipeId = recipeId;
        this.chefId = chefId;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.foodPic = foodPic;
        this.categoryId = categoryId;
    }

    public RecipeModel() {
        // no-arg constructor
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getFoodPic() {
        return foodPic;
    }

    public void setFoodPic(String foodPic) {
        this.foodPic = foodPic;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId; 
    }
    @Override
    public String toString() {
        return "RecipeModel{" +
                "recipeId=" + recipeId +
                ", chefId=" + chefId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", foodPic='" + foodPic + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }



    public double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }
    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }


}

