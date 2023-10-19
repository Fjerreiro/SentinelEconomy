package database;

import me.fjerreiro.sentineleconomy.OfferHelper;
import objects.SellOffer;
import org.bukkit.entity.Player;

import java.sql.*;

public class SellOfferDB {
    private Connection connection;

    public SellOfferDB(DatabaseConnection databaseConnection) {
        this.connection = databaseConnection.getConnection();
    }

    public boolean insertSellOffer(Player player, SellOffer sellOffer) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO selloffers (player, material, nbt, custom, price, totalqty, soldqty, money, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, sellOffer.getPlayer());
            preparedStatement.setString(2, sellOffer.getMaterial());
            preparedStatement.setDouble(5, sellOffer.getPrice());
            preparedStatement.setInt(6, sellOffer.getTotalqty());
            preparedStatement.setInt(7, sellOffer.getSoldQty());
            preparedStatement.setDouble(8, sellOffer.getMoney());
            preparedStatement.setString(9, sellOffer.getDate());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            OfferHelper.sendValidationMessage(player, "Something went wrong with committing your listing to the database! Contact an administrator.");
            System.out.println(e);
            return false;
        }
    }
}