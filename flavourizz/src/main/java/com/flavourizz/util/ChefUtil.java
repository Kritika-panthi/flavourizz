package com.flavourizz.util;

import com.flavourizz.config.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChefUtil {

    public static int getChefIdByEmail(String email) {
        String sql = "SELECT c.Chef_ID FROM chef c JOIN users u ON c.User_ID = u.User_ID WHERE LOWER(u.Email) = LOWER(?)AND u.Role_ID = 3";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("Chef_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
