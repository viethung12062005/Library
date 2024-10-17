/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package category.dao;

import category.model.Category;
import sqlite.Mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MysqlCategoryDao implements CategoryDao {

    private static MysqlCategoryDao instance;
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String GET_ALL = "SELECT * FROM category ";

    private MysqlCategoryDao() {

    }

    public static MysqlCategoryDao getInstance() {
        if (instance == null) {
            instance = new MysqlCategoryDao();
        }
        return instance;
    }

    @Override
    public List<Category> getAll() {
        List<Category> results = new ArrayList<>();
        try {
            Connection c = Mysql.getInstance().getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(GET_ALL);
            while (rs.next()) {
                results.add(createCategory(rs));
            }
            c.close();
            return results;
        } catch (SQLException e) {

        }
        return null;
    }

    private Category createCategory(ResultSet rs) throws SQLException {
        return new Category(rs.getString(COLUMN_ID), rs.getString(COLUMN_CATEGORY));

    }

}
