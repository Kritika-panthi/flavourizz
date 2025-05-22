<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.flavourizz.model.RecipeModel" %>
<%
@SuppressWarnings("unchecked")
    List<RecipeModel> recipes = (List<RecipeModel>) request.getAttribute("recipes");
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Recipes</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" 
          integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" 
          crossorigin="anonymous" referrerpolicy="no-referrer" />
          <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myrecipe.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    <h2>Your Recipes</h2>

    <table border="1">
        <tr>
            <th>Title</th>
            <th>Description</th>
            <th>Category</th>
            <th>Image</th>
            <th>Actions</th>
        </tr>
        <%
            if (recipes != null) {
                for (RecipeModel r : recipes) {
        %>
        <tr>
            <td><%= r.getTitle() %></td>
            <td><%= r.getDescription() %></td>
            <td><%= r.getCategoryId() %></td>
            <td><img src="${pageContext.request.contextPath}/resources/images/recipes/<%= r.getFoodPic() %>" alt="Recipe Image"width="150"height="150"/></td>
            <td>
                <form action="<%= request.getContextPath() %>/deleterecipe" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= r.getRecipeId() %>" />
                    <button type="submit" onclick="return confirm('Are you sure you want to delete this recipe?');">>Delete</button>
                </form>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
    <jsp:include page="footer.jsp" />
</body>
