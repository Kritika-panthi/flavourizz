package com.flavourizz.model;

public class Rating {
    private int userId;
    private int recipeId;
    private int score;

    public Rating(int userId, int recipeId, int score) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getScore() {
        return score;
    }
}
