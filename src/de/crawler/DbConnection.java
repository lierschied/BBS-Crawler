package de.crawler;

import java.sql.*;

public class DbConnection {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/test";
    private static final String dbUsername = "root";
    private static final String dbPassword = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
