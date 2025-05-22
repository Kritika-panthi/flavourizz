<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.flavourizz.model.UserModel" %>
<%
    List<UserModel> userList = (List<UserModel>) request.getAttribute("userList");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | Flavourizz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admindashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <script>
        function showTab(tabId) {
            var tabs = document.querySelectorAll('.tab-content');
            tabs.forEach(function(tab) {
                tab.style.display = 'none';
            });
            document.getElementById(tabId).style.display = 'block';

            var tabButtons = document.querySelectorAll('.tab-button');
            tabButtons.forEach(btn => btn.classList.remove('active'));
            document.getElementById(tabId + "-btn").classList.add('active');
        }

        window.onload = function() {
            var activeTab = '<%= request.getAttribute("activeTab") != null ? request.getAttribute("activeTab") : "dashboardTab" %>';
            showTab(activeTab);
        };
    </script>
</head>
<body>

    <jsp:include page="header.jsp" />

    <div class="dashboard-container">
        <h2>Welcome, <%= session.getAttribute("displayName") != null ? session.getAttribute("displayName") : "Admin" %></h2>

        <!-- Tabs -->
        <div class="tab-buttons">
            <button class="tab-button active" id="dashboardTab-btn" onclick="showTab('dashboardTab')">Dashboard</button>
            <button class="tab-button" id="usersTab-btn" onclick="showTab('usersTab')">Users List</button>
        </div>

        <!-- Dashboard Tab -->
        <div id="dashboardTab" class="tab-content">
            <div class="dashboard-cards">
                <div class="dashboard-card"><h3>Total Registered Users</h3><p>${totalUsers}</p></div>
                <div class="dashboard-card"><h3>Total Listed Recipes</h3><p>${totalRecipes}</p></div>
                <div class="dashboard-card"><h3>Rejected Comments</h3><p>${rejectedComments}</p></div>
                <div class="dashboard-card"><h3>Approved Comments</h3><p>${approvedComments}</p></div>
                <div class="dashboard-card"><h3>Unread Enquiries</h3><p>${unreadEnquiries}</p></div>
                <div class="dashboard-card"><h3>Read Enquiries</h3><p>${readEnquiries}</p></div>
                <div class="dashboard-card"><h3>Reports</h3><p><!-- Dynamic report info here --></p></div>
            </div>
        </div>

        <!-- Users List Tab -->
        <div id="usersTab" class="tab-content">
            <c:if test="${not empty message}">
                <div class="alert success-alert">${message}</div>
            </c:if>

            <div class="user-list-section">
                <h2>Registered Users and Chefs</h2>
                <table class="user-list-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (userList != null) {
                        for (UserModel user : userList) { %>
                            <tr>
                                <td><%= user.getUserId() %></td>
                                <td><%= user.getUsername() %></td>
                                <td><%= user.getEmail() %></td>
                                <td><%= user.getRoleId() == 3 ? "Chef" : "User" %></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/delete-user" method="post" onsubmit="return confirm('Are you sure you want to delete this user?');">
                                        <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                                        <button type="submit">Delete</button>
                                    </form>
                                </td>
                            </tr>
                    <%  }
                    } else { %>
                        <tr><td colspan="5">No users found.</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>