package com.flavourizz.service;

import com.flavourizz.config.DbConfig;
import com.flavourizz.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentService {

    // Add comment and link with recipe_comment bridging table
    public boolean addComment(Comment comment) {
        String insertCommentSQL = "INSERT INTO comment (Comment_Content, Created_At) VALUES (?, ?)";
        String insertRecipeCommentSQL = "INSERT INTO recipe_comment (User_ID, Recipe_ID, Comment_ID) VALUES (?, ?, ?)";

        try (Connection conn = DbConfig.getDbConnection()) {
            conn.setAutoCommit(false);

            // Insert into comment table
            try (PreparedStatement ps = conn.prepareStatement(insertCommentSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, comment.getContent());
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                int rows = ps.executeUpdate();

                if (rows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        comment.setCommentId(generatedKeys.getInt(1));
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Insert into bridging table
            try (PreparedStatement psBridge = conn.prepareStatement(insertRecipeCommentSQL)) {
                psBridge.setInt(1, comment.getUserId());
                psBridge.setInt(2, comment.getRecipeId());
                psBridge.setInt(3, comment.getCommentId());

                int bridgeRows = psBridge.executeUpdate();
                if (bridgeRows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve comments with username for display
    public List<Comment> getCommentsByRecipeId(int recipeId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.Comment_ID, c.Comment_Content, c.Created_At, u.Username " +
                "FROM comment c " +
                "JOIN recipe_comment rc ON c.Comment_ID = rc.Comment_ID " +
                "JOIN users u ON rc.User_ID = u.User_ID " +
                "WHERE rc.Recipe_ID = ? ORDER BY c.Created_At DESC";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Comment c = new Comment();
                c.setCommentId(rs.getInt("Comment_ID"));
                c.setContent(rs.getString("Comment_Content"));
                c.setCreatedAt(rs.getTimestamp("Created_At"));
                c.setUsername(rs.getString("Username"));
                comments.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comments;
    }
}
