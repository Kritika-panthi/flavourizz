package com.flavourizz.controller;

import com.flavourizz.model.Comment;
import com.flavourizz.model.UserModel;
import com.flavourizz.service.CommentService;
import com.flavourizz.service.LoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@SuppressWarnings("serial")
@WebServlet("/comment")
public class CommentController extends HttpServlet {
    private final CommentService commentService = new CommentService();
    private final LoginService loginService = new LoginService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            // Not logged in, redirect to login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = (String) session.getAttribute("username");
        UserModel user = loginService.getUserByUsername(username);

        if (user == null) {
            // User not found in DB, redirect to login (fail-safe)
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String content = request.getParameter("content");
        String recipeIdStr = request.getParameter("recipeId");
        int recipeId = 0;
        try {
            recipeId = Integer.parseInt(recipeIdStr);
        } catch (NumberFormatException ignored) {}

        if (content == null || content.trim().isEmpty() || recipeId == 0) {
            session.setAttribute("message", "Comment content or recipe ID missing.");
            response.sendRedirect(request.getContextPath() + "/singlerecipe?recipeId=" + recipeId);
            return;
        }

        Comment comment = new Comment();
        comment.setUserId(user.getUserId());
        comment.setRecipeId(recipeId);
        comment.setContent(content.trim());

        boolean success = commentService.addComment(comment);

        if (success) {
            session.setAttribute("message", "Comment posted successfully!");
        } else {
            session.setAttribute("message", "Failed to post comment. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/singlerecipe?recipeId=" + recipeId);
    }
}
