package com.flavourizz.service;

import com.flavourizz.config.DbConfig;
import com.flavourizz.model.CategoryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private Connection dbConn;

    public CategoryService() {
        try {
            this.dbConn = DbConfig.getDbConnection();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    /** 
     
Returns all categories from the Category table.*/
    public List<CategoryModel> listAll() {
        List<CategoryModel> cats = new ArrayList<>();
        String sql = "SELECT Category_ID, Category_Name FROM Category";

        try (Statement st = dbConn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                cats.add(new CategoryModel(
                    rs.getInt("Category_ID"),
                    rs.getString("Category_Name")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return cats;
    }

    public List<CategoryModel> getAllCategories() {
        List<CategoryModel> list = new ArrayList<>();
        String sql = "SELECT Category_ID, Category_Name FROM category";

        try (Connection conn = DbConfig.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoryModel cat = new CategoryModel();
                cat.setCategoryId(rs.getInt("Category_ID"));
                cat.setCategoryName(rs.getString("Category_Name"));
                list.add(cat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}