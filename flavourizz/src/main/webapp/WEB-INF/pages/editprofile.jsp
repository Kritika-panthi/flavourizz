<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editprofile.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>

<jsp:include page="header.jsp"/>
<c:if test="${not empty sessionScope.successMessage}">
  <div class="toast">${sessionScope.successMessage}</div>
  <c:remove var="successMessage" scope="session"/>
</c:if>
<div class="dashboard-container">
    <h2>Edit Profile</h2>
    <form method="post" action="${pageContext.request.contextPath}/user/edit-profile">
        <label for="username">New Username:</label><br>
        <input type="text" id="username" name="username" value="${user.username}" required><br><br>

        <label for="password">New Password:</label><br>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit" class="action-btn save-btn">Save Changes</button>
        <a href="${pageContext.request.contextPath}/user/dashboard" class="action-btn cancel-btn">Cancel</a>
    </form>
</div>
<jsp:include page="footer.jsp"/>

</body>
</html>