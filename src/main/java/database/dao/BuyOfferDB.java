package database.dao;

import database.connection.DatabaseConnection;

import java.sql.Connection;

public class BuyOfferDB {
    private Connection connection;

    public BuyOfferDB(DatabaseConnection databaseConnection) {
        this.connection = databaseConnection.getConnection();
    }
}