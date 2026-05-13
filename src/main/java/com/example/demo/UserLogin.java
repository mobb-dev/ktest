package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class UserLogin {

    private static final String DB_URL = "jdbc:sqlite:users.db";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (login(username, password)) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }
    }

    public static boolean login(String username, String password) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();

        String query = "SELECT * FROM users WHERE username = '" + username
                + "' AND password = '" + password + "'";

        ResultSet rs = stmt.executeQuery(query);
        boolean found = rs.next();

        rs.close();
        stmt.close();
        conn.close();
        return found;
    }
}
