<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" import="com.flavourizz.model.UserModel, com.flavourizz.model.CategoryModel, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Add Recipe</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addrecipe.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous" />
</head>
<body>

<jsp:include page="header.jsp" />

<div class="add-recipe-container">
    <h2>Add New Recipe</h2>

    <% if ("save".equals(request.getParameter("error"))) { %>
        <div class="error">Couldn’t save recipe. Please try again.</div>
    <% } %>

    <form action="${pageContext.request.contextPath}/addrecipe" method="post" enctype="multipart/form-data">
        <%
            UserModel currentUser = (UserModel) session.getAttribute("currentUser");
            int userId = currentUser != null ? currentUser.getUserId() : -1;
        %>
        <input type="hidden" name="chefId" value="<%= userId %>" />

        <div class="form-group">
            <label for="title">Recipe Title</label>
            <input type="text" id="title" name="title" required maxlength="150" value="<%= request.getParameter("title") != null ? request.getParameter("title") : "" %>">
        </div>

        <div class="form-group">
            <label for="description">Recipe Description</label>
            <textarea id="description" name="description" required><%= request.getParameter("description") != null ? request.getParameter("description") : "" %></textarea>
        </div>

        <div class="form-group">
            <label for="ingredients">Ingredients (comma-separated)</label>
            <textarea id="ingredients" name="ingredients" required><%= request.getParameter("ingredients") != null ? request.getParameter("ingredients") : "" %></textarea>
        </div>

        <div class="form-group">
            <label for="categoryId">Category</label>
            <select id="categoryId" name="categoryId" required>
                <option value="" disabled <%= request.getParameter("categoryId") == null ? "selected" : "" %>>Select Category</option>
                <%
                    List<CategoryModel> cats = (List<CategoryModel>) request.getAttribute("categories");
                    String selectedCatId = request.getParameter("categoryId");

                    if (cats != null) {
                        for (CategoryModel cat : cats) {
                            boolean selected = String.valueOf(cat.getCategoryId()).equals(selectedCatId);
                %>
                            <option value="<%= cat.getCategoryId() %>" <%= selected ? "selected" : "" %>>
                                <%= cat.getCategoryName() %>
                            </option>
                <%
                        }
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label for="foodPic">Photo</label>
            <input type="file" id="foodPic" name="foodPic" accept="image/*" required />
        </div>

        <button type="submit" class="submit-btn">Save Recipe</button>

        <div class="signin">
            <p><a href="${pageContext.request.contextPath}/myrecipes">View My Recipes</a></p>
        </div>
    </form>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
