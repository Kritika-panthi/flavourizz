<%@ page import="com.flavourizz.model.UserModel" %>
<%
    UserModel user = (UserModel) session.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Edit Chef Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registrationpage.css" />
</head>
<body>
<div class="box">
    <form action="${pageContext.request.contextPath}/editChefProfile" method="post" enctype="multipart/form-data">
        <h2>Edit Profile</h2>

        <div class="input-box">
            <input type="text" name="username" value="<%= user.getUsername() %>" placeholder="Username" required />
        </div>

        <div class="input-box">
            <input type="email" name="email" value="<%= user.getEmail() %>" placeholder="Email" readonly />
        </div>

        <div class="input-box">
            <textarea name="chefBio" placeholder="Bio" rows="3" required><%= request.getAttribute("chefBio") != null ? request.getAttribute("chefBio") : "" %></textarea>
        </div>

        <div class="input-box">
            <input type="text" name="socialLinks" placeholder="Social Links (comma separated)"
                   value="<%= request.getAttribute("socialLinks") != null ? request.getAttribute("socialLinks") : "" %>" />
        </div>

        <div class="input-box">
            <input type="file" name="profilePic" accept="image/*" />
        </div>

        <hr/>
        <h3>Change Password</h3>

        <div class="input-box">
            <input type="password" name="currentPassword" placeholder="Current Password" />
        </div>

        <div class="input-box">
            <input type="password" name="newPassword" placeholder="New Password" />
        </div>

        <div class="input-box">
            <input type="password" name="confirmNewPassword" placeholder="Confirm New Password" />
        </div>

        <% if(request.getAttribute("errorMessage") != null) { %>
            <div class="error-message"><%= request.getAttribute("errorMessage") %></div>
        <% } %>

        <button type="submit" class="register-button">Save Changes</button>
    </form>
</div>
</body>
</html>