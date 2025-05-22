<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.flavourizz.model.RecipeModel" %>

<%
    List<RecipeModel> recipes = (List<RecipeModel>) request.getAttribute("recipes");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Recipes | Flavouriz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="headerRep">
   <img src="${pageContext.request.contextPath}/images/system/spring rolls.jiff" alt="Recipe Search Banner" class="header-image">
</div>

<div class="search-container">
    <form action="${pageContext.request.contextPath}/search" method="get" style="display: flex; align-items: center;">
        <input type="text" name="q" id="searchInput" placeholder="Search dishes..." value="${q != null ? q : ''}">
        <button type="submit" id="search"><i class="fa-solid fa-magnifying-glass"></i></button>
    </form>
</div>


<div class="gallery">
    <%
        if (recipes != null && !recipes.isEmpty()) {
            for (RecipeModel r : recipes) {
    %>
        <div class="card">
            <a href="<%= request.getContextPath() %>/singlerecipe?recipeId=<%= r.getRecipeId() %>">
                <img src="<%= request.getContextPath() %>/resources/images/recipes/<%= r.getFoodPic() %>" alt="<%= r.getTitle() %>">
                <div class="card-body">
			    <h2><%= r.getTitle() %></h2>
			    <p>		    
			<% Double rating = r.getAverageRating(); %>
				<b>Rating:</b>
				<% if (r.getTotalRatings() > 0) { %>
				    <%= String.format("%.1f", r.getAverageRating()) %> / 5
				    <div class="stars">
				        <%
				            int fullStars = (int) r.getAverageRating();
				            boolean half = (r.getAverageRating() - fullStars) >= 0.5;
				            for (int i = 0; i < fullStars; i++) {
				        %>
				            <i class="fa-solid fa-star"></i>
				        <% } %>
				        <% if (half) { %>
				            <i class="fa-solid fa-star-half-stroke"></i>
				        <% } %>
				        <% for (int i = fullStars + (half ? 1 : 0); i < 5; i++) { %>
				            <i class="fa-regular fa-star"></i>
				        <% } %>
				    </div>
				<% } else { %>
				    Not rated yet
				<% } %>
				</div>
            </a>
        </div>
    <%
            }
        } else {
    %>
        <p style="text-align:center; width: 100%;">No recipes found.</p>
    <%
        }
    %>
</div>


<jsp:include page="footer.jsp" />

</body>
</html>