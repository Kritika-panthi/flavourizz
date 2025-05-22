<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
 <meta charset="UTF-8">
 <meta name="viewport" content="width=device-width, initial-scale=1.0">
 <title>Login page</title>
 <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginpage.css">
</head>
<body>
    <div class="container-login">
        <form action="${pageContext.request.contextPath}/login" method="post">
<h2 style="color: black;">Get Recipe with Flavouriz</h2>

            <div class="form-box">
                <input type="email" name="email" placeholder="E-mail" required
                       value="${param.email != null ? param.email : ''}">
            </div>

            <div class="form-box">
                <input type="password" name="password" placeholder="Password" required>
            </div>

      <div class="forgot" style="color: black;">
    <label><input type="checkbox" name="rememberMe">Remember me</label>
    <a href="#" style="color: black;">Forgot Password?</a>
</div>  

            <!-- Show error message if exists -->
            <div style="color: red; margin-bottom: 10px;">
                ${requestScope.error != null ? requestScope.error : ''}
            </div>

            <button type="submit" class="signin-button">Sign In</button>

        
<div class="register" style="color: black;">
    <p>Don't have an account? 
    <a href="${pageContext.request.contextPath}/register" style="color: black;">Register</a></p>
</div>
        </form> 
    </div>
</body>
</html>