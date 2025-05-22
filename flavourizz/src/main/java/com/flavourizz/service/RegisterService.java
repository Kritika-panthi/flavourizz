package com.flavourizz.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.flavourizz.config.DbConfig;
import com.flavourizz.model.UserModel;

public class RegisterService {

    private Connection dbConn;
    private boolean isConnectionError = false;

    public RegisterService() {
        try {
            dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            isConnectionError = true;
        }
    }

    public boolean registerUser(UserModel user) {
        if (isConnectionError) {
            System.out.println("DB connection error during registration.");
            return false;
        }

        // Check if email exists
        String checkEmailQuery = "SELECT email FROM users WHERE LOWER(email) = LOWER(?)";
        try (PreparedStatement checkStmt = dbConn.prepareStatement(checkEmailQuery)) {
            checkStmt.setString(1, user.getEmail().trim().toLowerCase());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Email already exists: " + user.getEmail());
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Insert user
        String insertQuery = "INSERT INTO users (Username, Email, Password, Role_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStmt = dbConn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, user.getUsername().trim());
            insertStmt.setString(2, user.getEmail().trim());
            insertStmt.setString(3, user.getPassword());
            insertStmt.setInt(4, user.getRoleId());

            int rowsInserted = insertStmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- New method: getUserIdByEmail ---
    public int getUserIdByEmail(String email) {
        if (isConnectionError) return -1;

        int userId = -1;
        String sql = "SELECT User_ID FROM users WHERE LOWER(email) = LOWER(?)";
        try (PreparedStatement ps = dbConn.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("User_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    // --- New method: registerChef ---
    public boolean registerChef(int userId, String bio, String profilePicFileName, String socialLinks) {
        if (isConnectionError) return false;

        String sql = "INSERT INTO chef (User_ID, Chef_Bio, Chef_Profile_Pic, Social_Links) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = dbConn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, bio);
            ps.setString(3, profilePicFileName);
            ps.setString(4, socialLinks);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateUser(UserModel user) {
        String sql = "UPDATE users SET Username=?, Email=? WHERE User_ID=?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateChef(int userId, String bio, String profilePic, String socialLinks) {
        String sql = "UPDATE chef SET Chef_Bio=?, Chef_Profile_Pic=?, Social_Links=? WHERE User_ID=?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bio);
            ps.setString(2, profilePic);
            ps.setString(3, socialLinks);
            ps.setInt(4, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserPassword(int userId, String encryptedPassword) {
        String sql = "UPDATE users SET Password=? WHERE User_ID=?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, encryptedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, String> getChefDetails(int userId) {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT Chef_Bio, Social_Links FROM chef WHERE User_ID=?";
        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                map.put("bio", rs.getString("Chef_Bio"));
                map.put("links", rs.getString("Social_Links"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}	