package com.flavourizz.controller;

import com.flavourizz.model.UserModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin-dashboard")
public class AdminDashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Dummy data - replace with real DB queries if needed
        int totalUsers = 1245;
        int totalRecipes = 512;
        int rejectedComments = 37;
        int approvedComments = 1128;
        int unreadEnquiries = 24;
        int readEnquiries = 56;

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalRecipes", totalRecipes);
        request.setAttribute("rejectedComments", rejectedComments);
        request.setAttribute("approvedComments", approvedComments);
        request.setAttribute("unreadEnquiries", unreadEnquiries);
        request.setAttribute("readEnquiries", readEnquiries);

        // Get users from DB (excluding admins)
        List<UserModel> userList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // or your driver
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/flavouriz1", "root", ""); // change to match your DB
            String sql = "SELECT * FROM users WHERE Role_ID IN (1, 3)"; // 1=user, 3=chef
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserModel user = new UserModel();
                user.setUserId(rs.getInt("User_ID"));
                user.setUsername(rs.getString("Username"));
                user.setEmail(rs.getString("Email"));
                user.setRoleId(rs.getInt("Role_ID"));
                userList.add(user);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("userList", userList);

        // Get admin name
        HttpSession session = request.getSession(false);
        String adminName = (session != null && session.getAttribute("adminUser") != null)
                ? (String) session.getAttribute("adminUser") : "Admin";
        if (session != null) {
            String message = (String) session.getAttribute("message");
            if (message != null) {
                request.setAttribute("message", message);
                session.removeAttribute("message");  // clear so it doesn't show again
            }
        }
        String activeTab = request.getParameter("tab");
        if (activeTab == null || activeTab.isEmpty()) {
            activeTab = "dashboardTab"; // Default tab
        }
        request.setAttribute("activeTab", activeTab);
        request.getRequestDispatcher("/WEB-INF/pages/admindashboard.jsp").forward(request, response);
    }
}