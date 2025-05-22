<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Flavourizz Header</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" />
</head>
<body>
  <header>
    <div class="container">
      <a href="${pageContext.request.contextPath}/home" class="logo-link">
<img src="${pageContext.request.contextPath}/resources/images/system/logoo.png" alt="Flavourizz Logo" class="logo-img" />
      </a>
<nav>

        <ul class="nav-bar">

            <li><a href="${pageContext.request.contextPath}/home">Home</a></li>

            <li><a href="${pageContext.request.contextPath}/search">Recipe</a></li>

            <li><a href="${pageContext.request.contextPath}/category">Category</a></li>

            <li><a href="${pageContext.request.contextPath}/contact">Contact Us</a></li>

            <li><a href="${pageContext.request.contextPath}/about">About Us</a></li>

        </ul>

  <ul class="nav-icons">
    <li><a href="${pageContext.request.contextPath}/search"><i class="fa-solid fa-magnifying-glass"></i></a></li>
        <ul class="nav-icons">
          <!-- Show username and logout if logged in -->
          <c:if test="${not empty sessionScope.username}">
            <li>
              <a href="${pageContext.request.contextPath}/${sessionScope.role == 'admin' ? 'admin-dashboard' : (sessionScope.role == 'chef' ? 'chef/dashboard' : 'user/dashboard')}" 
   class="header-username-link">
   <i class="fa-solid fa-user"></i> ${sessionScope.username}
</a>
         </li>
            <li>
              <a href="${pageContext.request.contextPath}/logout" class="logout-button">
                <i class="fa-solid fa-right-from-bracket"></i> Logout
              </a>
            </li>
          </c:if>

          <!-- Show login link if NOT logged in -->
          <c:if test="${empty sessionScope.username}">
            <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
          </c:if>
        </ul>
      </nav>
    </div>
  </header>
</body>
</html>