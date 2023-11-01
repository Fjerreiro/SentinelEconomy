package database.dao;

import database.connection.DatabaseConnection;
import me.fjerreiro.sentineleconomy.OfferHelper;
import objects.BuyOffer;
import objects.SellOffer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BuyOfferDB {
    private Connection connection;

    public BuyOfferDB(DatabaseConnection databaseConnection) {
        this.connection = databaseConnection.getConnection();
    }

    public boolean insertBuyOffer(Player player, BuyOffer buyOffer) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO buyoffers (player, material, nbt, custom, price, totalqty, boughtqty, money, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, buyOffer.getPlayer());
            preparedStatement.setString(2, buyOffer.getMaterial());
            preparedStatement.setDouble(5, buyOffer.getPrice());
            preparedStatement.setInt(6, buyOffer.getTotalqty());
            preparedStatement.setInt(7, buyOffer.getBoughtqty());
            preparedStatement.setDouble(8, buyOffer.getMoney());
            preparedStatement.setString(9, buyOffer.getDate());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            OfferHelper.sendValidationMessage(player, "Something went wrong with committing your listing to the database! Contact an administrator.");
            System.out.println(e);
            return false;
        }
    }
}
