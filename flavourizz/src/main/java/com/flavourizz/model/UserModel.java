package com.flavourizz.model;

public class UserModel {
    private int userId;        // Maps to User_ID
    private String username;   // Maps to Username
    private String email;      // Maps to Email
    private String password;   // Maps to Password
    private int roleId;        // Maps to Role_ID

    // Constructors
    public UserModel() {}

    public UserModel(int userId, String username, String email, String password, int roleId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    public UserModel(String username, String email, String password, int roleId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase(); // Normalize email
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}