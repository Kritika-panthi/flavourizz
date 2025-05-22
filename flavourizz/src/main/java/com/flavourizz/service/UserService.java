package com.flavourizz.service;

import com.flavourizz.model.UserModel;
import com.flavourizz.config.DbConfig;

import java.sql.*;

public class UserService {

    public UserModel getUserByUsername(String username) {
        try (Connection con = DbConfig.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE Username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserModel user = new UserModel();
                user.setUserId(rs.getInt("User_ID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                user.setRoleId(rs.getInt("Role_ID"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(UserModel user) {
        try (Connection con = DbConfig.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE users SET Username = ?, Password = ? WHERE User_ID = ?"
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getUserId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
