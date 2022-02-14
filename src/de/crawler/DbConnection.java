package de.crawler;

import java.sql.*;

public class DbConnection {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/opc";
    private static final String dbUsername = "root";
    private static final String dbPassword = "password";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        }
        return connection;
    }
}
