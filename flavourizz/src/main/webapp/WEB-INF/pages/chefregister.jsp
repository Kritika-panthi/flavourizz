<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Chef Registration</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/registrationpage.css" />
</head>
<body>
    <div class="box">
        <form action="${pageContext.request.contextPath}/chefregister" method="post" enctype="multipart/form-data">
            <h2>Chef Register</h2>

            <div class="input-box">
                <input type="text" name="username" placeholder="Username" value="${username != null ? username : ''}" required />
            </div>
            <div class="input-box">
                <input type="email" name="email" placeholder="E-mail" value="${email != null ? email : ''}" required />
            </div>
            <div class="input-box">
                <input type="password" name="password" placeholder="Password" required />
            </div>
            <div class="input-box">
                <input type="password" name="retypePassword" placeholder="Confirm Password" required />
            </div>

            <div class="input-box">
                <textarea name="chefBio" placeholder="Brief Bio" rows="3" required>${chefBio != null ? chefBio : ''}</textarea>
            </div>
            <div class="input-box">
                <input type="file" name="profilePic" accept="image/*" />
            </div>
            <div class="input-box">
                <input type="text" name="socialLinks" placeholder="Social Links (comma separated)" value="${socialLinks != null ? socialLinks : ''}" />
            </div>

            <% if(request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("errorMessage") %>
            </div>
            <% } %>

            <button type="submit" class="register-button">Register</button>
            <div class="signin">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
            </div>
        </form>
    </div>
</body>
</html>