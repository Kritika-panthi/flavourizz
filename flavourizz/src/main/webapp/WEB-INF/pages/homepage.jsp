<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.List, com.flavourizz.model.RecipeModel" %>
<%
    List<RecipeModel> randomRecipes = (List<RecipeModel>) request.getAttribute("randomRecipes");
    List<RecipeModel> trendingRecipes = (List<RecipeModel>) request.getAttribute("trendingRecipes");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Flavouriz - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homepage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
</head>
<body>

<jsp:include page="header.jsp" />

<!-- Banner Section -->
<div class="image-container">
    <img src="${pageContext.request.contextPath}/images/f.PNG" alt="food" class="background-image">
    <div class="text">
        <h1>Discover Your Next Favorite Meal</h1>
        <p>Explore, cook, and create unforgettable memories</p>
        <p>with dishes that are as enjoyable to make as they are to eat.</p>
        <button class="explore-btn">Explore More</button>
    </div>
</div>

<!-- FAVOURITE RECIPES -->
<div class="heading">FAVOURITE RECIPES</div>
<div class="gallery">
<% if (randomRecipes != null && !randomRecipes.isEmpty()) {
     for (RecipeModel r : randomRecipes) { %>
    <div class="card">
        <a href="<%= request.getContextPath() %>/singlerecipe?recipeId=<%= r.getRecipeId() %>">
            <img src="<%= request.getContextPath() %>/resources/images/recipes/<%= r.getFoodPic() %>" alt="<%= r.getTitle() %>">
            <div class="card-body">
                <h2><%= r.getTitle() %></h2>
            </div>
        </a>
    </div>
<%  }
   } else { %>
   <p style="text-align:center; width:100%;">No favorite recipes found.</p>
<% } %>
</div>

<!-- TRENDING NOW -->
<div class="heading">TRENDING NOW</div>
<div class="gallery">
    <%
        if (trendingRecipes != null && !trendingRecipes.isEmpty()) {
            for (RecipeModel r : trendingRecipes) {
    %>
    <div class="card">
        <a href="<%= request.getContextPath() %>/singlerecipe?recipeId=<%= r.getRecipeId() %>">
            <img src="<%= request.getContextPath() %>/resources/images/recipes/<%= r.getFoodPic() %>" alt="<%= r.getTitle() %>">
        </a>
        <div class="card-body">
            <h2><%= r.getTitle() %></h2>
            <p><strong>Rating:</strong>
                <% if (r.getTotalRatings() > 0) { %>
                    <%= String.format("%.1f", r.getAverageRating()) %> / 5
                    <div class="stars">
                        <%
                            int fullStars = (int) r.getAverageRating();
                            boolean halfStar = (r.getAverageRating() - fullStars) >= 0.5;
                            for (int i = 0; i < fullStars; i++) { %>
                                <i class="fa-solid fa-star"></i>
                        <% } %>
                        <% if (halfStar) { %>
                            <i class="fa-solid fa-star-half-stroke"></i>
                        <% } %>
                        <% for (int i = fullStars + (halfStar ? 1 : 0); i < 5; i++) { %>
                            <i class="fa-regular fa-star"></i>
                        <% } %>
                    </div>
                <% } else { %>
                    Not rated yet
                <% } %>
            </p>
        </div>
    </div>
    <%
            }
        } else {
    %>
    <p style="text-align:center;">No trending recipes available.</p>
    <%
        }
    %>
</div>

<jsp:include page="footer.jsp" />
</body>
</html>
