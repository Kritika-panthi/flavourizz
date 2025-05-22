package com.flavourizz.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.flavourizz.config.DbConfig;
import com.flavourizz.model.UserModel;

public class LoginService {

    public UserModel getUserByEmail(String email) {
        UserModel user = null;
        String sql = "SELECT User_ID, Username, email, Password, Role_ID FROM users WHERE email = ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new UserModel();

                user.setUserId(rs.getInt("User_ID"));       // updated column name
                user.setUsername(rs.getString("Username")); // updated column name
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("Password")); // updated column name
                user.setRoleId(rs.getInt("Role_ID"));       // updated column name
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
 
    public UserModel getUserByUsername(String username) {
        UserModel user = null;
        String sql = "SELECT User_ID, Username, email, Password, Role_ID FROM users WHERE Username = ?";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new UserModel();
                user.setUserId(rs.getInt("User_ID"));
                user.setUsername(rs.getString("Username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("Password"));
                user.setRoleId(rs.getInt("Role_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    
}