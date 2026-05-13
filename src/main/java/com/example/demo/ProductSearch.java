package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductSearch {

    private static final String DB_URL = "jdbc:sqlite:shop.db";

    public static void searchByName(String name) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();

        String query = "SELECT id, name, price FROM products WHERE name LIKE '%"
                + name + "%'";

        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | " + rs.getString("name")
                    + " | " + rs.getDouble("price"));
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public static void deleteById(String id) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM products WHERE id = " + id);
        stmt.close();
        conn.close();
    }
}
