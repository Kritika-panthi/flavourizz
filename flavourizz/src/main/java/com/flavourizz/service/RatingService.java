package com.flavourizz.service;

import com.flavourizz.config.DbConfig;
import com.flavourizz.model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RatingService {

    public boolean submitRating(Rating rating) {
        String checkQuery = "SELECT * FROM rating WHERE User_ID = ? AND Recipe_ID = ?";
        String insertQuery = "INSERT INTO rating (User_ID, Recipe_ID, Score) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE rating SET Score = ? WHERE User_ID = ? AND Recipe_ID = ?";

        try (Connection conn = DbConfig.getDbConnection()) {
            // Check if rating exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, rating.getUserId());
                checkStmt.setInt(2, rating.getRecipeId());

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    // Update existing
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, rating.getScore());
                        updateStmt.setInt(2, rating.getUserId());
                        updateStmt.setInt(3, rating.getRecipeId());
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // Insert new
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, rating.getUserId());
                        insertStmt.setInt(2, rating.getRecipeId());
                        insertStmt.setInt(3, rating.getScore());
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public double getAverageRating(int recipeId) {
        String query = "SELECT AVG(Score) AS avgScore FROM rating WHERE Recipe_ID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double val = rs.getDouble("avgScore");
                if (rs.wasNull()) {
                    return 0.0;  // explicitly handle SQL NULL
                }
                return val;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }


    public int getTotalRatings(int recipeId) {
        String query = "SELECT COUNT(*) AS total FROM rating WHERE Recipe_ID = ?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, recipeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("total");
                return rs.wasNull() ? 0 : count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    
}

