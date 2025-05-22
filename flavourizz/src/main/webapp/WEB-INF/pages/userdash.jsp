<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>User Dashboard | Flavouriz</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"/>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userdash.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>

  <jsp:include page="header.jsp" />

<div class="dashboard-container">

  <div class="dashboard-header">
    <div>
      <h2>Welcome back, <span class="username">${sessionScope.username}</span>!</h2>
      <p>Explore your saved favorites and activity 🍽️</p>
    </div>
  </div>

  <div class="dashboard-cards">
    <div class="dashboard-card">
      <i class="fa-solid fa-book-open"></i>
      <h3>Saved Recipes</h3>
      <p>12</p>
    </div>
    
    <div class="dashboard-card">
      <i class="fa-solid fa-comment-dots"></i>
      <h3>Comments</h3>
      <p>23</p>
    </div>
  </div>

  <div class="dashboard-actions">
    <h3>Quick Actions</h3>
<button class="action-btn" onclick="location.href='${pageContext.request.contextPath}/user/edit-profile'">Edit Profile</button>
    <button class="action-btn">View My Favorites</button>
    <button class="action-btn delete-btn">Delete Account</button>
  </div>

  <div class="dashboard-activity">
    <h3>Recent Activity</h3>
    <ul>
      <li>You saved <strong>Momo</strong> to your favorites</li>
      <li>You liked <strong>Butter Chicken</strong></li>
      <li>You commented on <strong>Gulab Jamun</strong></li>
    </ul>
  </div>

</div>
<jsp:include page="footer.jsp" />

</body>
</html>