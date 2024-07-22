package me.fjerreiro.sentineleconomy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private Connection connection;

    public DatabaseConnection(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        Statement statement = connection.createStatement();
        statement.execute("""
                CREATE TABLE IF NOT EXISTS selloffers (
                uuid INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
                player TEXT NOT NULL,
                material TEXT NOT NULL,
                nbt TEXT,
                custom BOOLEAN DEFAULT FALSE,
                price REAL NOT NULL,
                totalqty INTEGER NOT NULL,
                soldqty INTEGER NOT NULL,
                money REAL DEFAULT 0,
                date TEXT NOT NULL)               
                """);
        statement.execute("""
                CREATE TABLE IF NOT EXISTS buyoffers (
                uuid INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
                player TEXT NOT NULL,
                material TEXT NOT NULL,
                nbt TEXT,
                custom BOOLEAN DEFAULT FALSE,
                price REAL NOT NULL,
                totalqty INTEGER NOT NULL,
                boughtqty INTEGER NOT NULL,
                money REAL DEFAULT 0,
                date TEXT NOT NULL)
                """);
        statement.close();
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
