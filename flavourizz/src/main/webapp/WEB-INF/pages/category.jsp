<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.flavourizz.model.RecipeModel, com.flavourizz.model.CategoryModel" %>
<%
    List<RecipeModel> recipes = (List<RecipeModel>) request.getAttribute("recipes");
    List<CategoryModel> categories = (List<CategoryModel>) request.getAttribute("categories");
    Integer selectedCategoryId = (Integer) request.getAttribute("selectedCategoryId");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Category | Flavouriz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="headerCat">
    Category
    <div class="headerCatbody">
        Explore delicious recipes under this category!
    </div>
</div>

<!-- Category dropdown -->
<div class="filter-bar">
    <form action="${pageContext.request.contextPath}/category" method="get">
        <select name="categoryId" onchange="this.form.submit()">
            <option value="">-- Select Category --</option>
            <%
                for (CategoryModel cat : categories) {
                    String selected = (selectedCategoryId != null && selectedCategoryId == cat.getCategoryId()) ? "selected" : "";
            %>
                <option value="<%= cat.getCategoryId() %>" <%= selected %>><%= cat.getCategoryName() %></option>
            <%
                }
            %>
        </select>
    </form>
</div>

<!-- Recipe Gallery -->
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
                </div>
            </a>
        </div>
    <%
            }
        } else {
    %>
        <p style="text-align:center; width:100%; font-size:18px;">No recipes found for this category.</p>
    <%
        }
    %>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
