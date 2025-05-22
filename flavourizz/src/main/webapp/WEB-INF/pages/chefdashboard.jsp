<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.flavourizz.model.RecipeModel" %>
<%
    com.flavourizz.model.UserModel currentUser = (com.flavourizz.model.UserModel) session.getAttribute("currentUser");
    String displayName = (currentUser != null) ? currentUser.getUsername() : "Chef";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chef Dashboard | Flavouriz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chefdash.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" 
          crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>
<jsp:include page="/WEB-INF/pages/header.jsp" />

<main class="dashboard">
<section class="welcome">
    <h2>Welcome back, <%= displayName %>!</h2>
    <p>Manage your recipes and activity below!</p>
</section>

    <section class="cards">
        <div class="card">
            <div class="card-icon">🍴</div>
            <h3><a href="${pageContext.request.contextPath}/addrecipe">Add Recipes</a></h3>
        </div>
        <div class="card">
            <div class="card-icon">🗑️</div>
            <h3><a href="${pageContext.request.contextPath}/myrecipes">Remove Recipes</a></h3>
        </div>
    </section>

    <section class="quick-actions">
        <h3>Quick Actions</h3>
        <div class="action-buttons">
		<button onclick="location.href='${pageContext.request.contextPath}/editChefProfile'" class="btn">Edit Profile</button>


            <form action="<%= request.getContextPath() %>/deleteChef" method="post" style="danger;">
    <button type="submit"
            onclick="return confirm('Are you sure you want to delete your account? This will remove all your recipes permanently.');"
            class="danger">
        Delete Account
    </button>
</form>
        </div>
    </section>
<%
    List<RecipeModel> recipes = (List<RecipeModel>) request.getAttribute("recipes");
%>
<section class="gallery">
<%
    if (recipes != null && !recipes.isEmpty()) {
        for (RecipeModel r : recipes) {
%>
    <div class="card">
       <a href="<%= request.getContextPath() %>/singlerecipe?recipeId=<%= r.getRecipeId() %>">
            <img src="<%= request.getContextPath() %>/resources/images/recipes/<%= r.getFoodPic() %>" alt="Recipe Image" />
            <h4><%= r.getTitle() %></h4>
        </a>
    </div>
<%
        }
    } else {
%>
    <p style="padding-left: 40px;">You haven’t added any recipes yet.</p>
<%
    }
%>
</section>

</main>
<jsp:include page="footer.jsp" />
</body>
</html>