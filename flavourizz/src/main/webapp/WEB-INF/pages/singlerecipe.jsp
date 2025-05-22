<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.flavourizz.model.Comment" %>

<%
    String username = null;
    String message = null;

    if (session != null) {
        Object uname = session.getAttribute("username");
        if (uname != null) {
            username = uname.toString();
        }

        Object msg = session.getAttribute("message");
        if (msg != null) {
            message = msg.toString();
            session.removeAttribute("message");
        }
    }

    String title = (String) request.getAttribute("title");
    String description = (String) request.getAttribute("description");
    String ingredients = (String) request.getAttribute("ingredients");
    String foodPic = (String) request.getAttribute("foodPic");
    Integer recipeId = (Integer) request.getAttribute("recipeId");
    

    if (title == null) title = "Recipe Not Found";
    if (description == null) description = "";
    if (ingredients == null) ingredients = "";
    if (recipeId == null) recipeId = 0;

    List<Comment> comments = (List<Comment>) request.getAttribute("comments");
    if (comments == null) {
        comments = new java.util.ArrayList<>();
    }

    Double averageRating = (Double) request.getAttribute("averageRating");
    Integer totalRatings = (Integer) request.getAttribute("totalRatings");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title><%= title %> - Recipe</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/singlerecipe.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/header.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/footer.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" crossorigin="anonymous" />
</head>
<body>

<jsp:include page="header.jsp" />

<div class="recipe-container">
    <h1><%= title %></h1>

    <% if (foodPic != null && !foodPic.isEmpty()) { %>
        <img src="<%= request.getContextPath() %>/resources/images/recipes/<%= foodPic %>" alt="Recipe Image" />
    <% } else { %>
        <p>No image available.</p>
    <% } %>

    <p>
        <strong>Chef:</strong> <%= request.getAttribute("chefName") %><br/>
        <strong>Category:</strong> <%= request.getAttribute("categoryName") %><br/>
    </p>

    <section class="description">
        <h2>Description</h2>
        <p><%= description %></p>
    </section>

    <section class="ingredients">
        <h2>Ingredients</h2>
        <ul>
            <%
                if (!ingredients.trim().isEmpty()) {
                    for (String ingredient : ingredients.split(",")) {
            %>
                <li><%= ingredient.trim() %></li>
            <%
                    }
                } else {
            %>
                <li>No ingredients listed.</li>
            <%
                }
            %>
        </ul>
    </section>

    <!-- Rating Summary Section -->
    <div class="rating-summary">
        <h3>Recipe Rating</h3>
        <% if (averageRating != null && totalRatings != null && totalRatings > 0) { %>
            <p>
                <strong><%= String.format("%.1f", averageRating) %></strong> out of 5 
                (<%= totalRatings %> rating<%= totalRatings > 1 ? "s" : "" %>)
            </p>
            <div class="stars">
                <% 
                    int fullStars = averageRating.intValue();
                    boolean halfStar = (averageRating - fullStars) >= 0.5;
                    for (int i = 0; i < fullStars; i++) {
                %>
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
            <p>This recipe has not been rated yet.</p>
        <% } %>
    </div>

    <% if (username != null) { %>
        <div class="rating-form">
            <h3>Rate this Recipe</h3>
            <% if (message != null) { %>
                <p class="message"><%= message %></p>
            <% } %>
            <form action="<%= request.getContextPath() %>/rate" method="post">
                <input type="hidden" name="username" value="<%= username %>" />
                <input type="hidden" name="recipeId" value="<%= recipeId %>" />
                <label for="score">Select Rating (1 to 5):</label>
                <select name="score" id="score" required>
                    <option value="" disabled selected>Select your rating</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
                <button type="submit">Submit Rating</button>
            </form>
        </div>
    <% } else { %>
        <p class="login-warning">
            Please <a href="<%= request.getContextPath() %>/login">login</a> to rate this recipe.
        </p>
    <% } %>
</div>

<!-- Comments Section -->
<div class="comments-section">
    <h3>Comments</h3>

    <% if (username != null) { %>
        <form action="<%= request.getContextPath() %>/comment" method="post" class="comment-form">
            <input type="hidden" name="recipeId" value="<%= recipeId %>">
            <textarea name="content" placeholder="Write your comment here..." required></textarea>
            <button type="submit">Post Comment</button>
        </form>
    <% } else { %>
        <p class="login-warning">Please <a href="<%= request.getContextPath() %>/login">login</a> to comment.</p>
    <% } %>

    <% for (Comment c : comments) { %>
        <div class="comment">
            <p>
                <strong><%= c.getUsername() %></strong>
                <span class="comment-date">(<%= c.getCreatedAt() %>)</span>
            </p>
            <p><%= c.getContent() %></p>
        </div>
    <% } %>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
