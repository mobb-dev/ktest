package com.example.sqli;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) throws Exception {
        String username = args.length > 0 ? args[0] : "admin";

        Connection conn = DriverManager.getConnection(
            "jdbc:h2:mem:demo", "sa", "");

        Statement stmt = conn.createStatement();

        // Vulnerable: user input concatenated directly into SQL.
        String query = "SELECT id, email FROM users WHERE username = '" + username + "'";
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            System.out.println(rs.getInt("id") + " " + rs.getString("email"));
        }

        rs.close();
        stmt.close();
        conn.close();
    }
}
