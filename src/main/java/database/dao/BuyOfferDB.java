package database.dao;

import database.connection.DatabaseConnection;
import me.fjerreiro.sentineleconomy.OfferHelper;
import objects.BuyOffer;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuyOfferDB {
    private Connection connection;

    public BuyOfferDB(DatabaseConnection databaseConnection) {
        this.connection = databaseConnection.getConnection();
    }

    // Function to get a single BuyOffer from the database and return it
    public BuyOffer retrieveBuyOffer(Player cmdSender, String sqlQuery) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Initialize attributes and create a BuyOffer object
                String uuid = resultSet.getString("uuid");
                String player = resultSet.getString("player");
                String material = resultSet.getString("material");
                String nbt = resultSet.getString("nbt");
                boolean custom = resultSet.getBoolean("custom");
                double price = resultSet.getDouble("price");
                int totalqty = resultSet.getInt("totalqty");
                int boughtqty = resultSet.getInt("boughtqty");
                double money = resultSet.getDouble("money");
                String date = resultSet.getString("date");

                BuyOffer buyOffer = new BuyOffer(uuid, player, material, nbt, custom, price, totalqty, boughtqty, money, date);

                return buyOffer;
            }
        } catch (SQLException e) {
            OfferHelper.sendValidationMessage(cmdSender, "Something went wrong with retrieving buyoffers from the database. Contact an admin!");
        }
        return null;
    }

    // Function to retrieve BuyOffers from the database and return them in an ArrayList
    public List<BuyOffer> retrieveBuyOfferList(Player cmdSender, String sqlQuery) {

        // Create empty ArrayList to add BuyOffer to
        List<BuyOffer> buyOfferList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop over all the results and make BuyOffer objects and add them to the ArrayList
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String player = resultSet.getString("player");
                String material = resultSet.getString("material");
                String nbt = resultSet.getString("nbt");
                boolean custom = resultSet.getBoolean("custom");
                double price = resultSet.getDouble("price");
                int totalqty = resultSet.getInt("totalqty");
                int boughtqty = resultSet.getInt("boughtqty");
                double money = resultSet.getDouble("money");
                String date = resultSet.getString("date");

                BuyOffer buyOffer = new BuyOffer(uuid, player, material, nbt, custom, price, totalqty, boughtqty, money, date);

                buyOfferList.add(buyOffer);
                return buyOfferList;
            }
        } catch (SQLException e) {
            OfferHelper.sendValidationMessage(cmdSender, "Something went wrong with retrieving buyoffers from the database. Contact an admin!");
        }
        return null;
    }

    // Function to insert a single BuyOffer into the database
    public boolean insertBuyOffer(Player cmdSender, BuyOffer buyOffer) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO buyoffers (player, material, nbt, custom, price, totalqty, boughtqty, money, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            // Set the values for the SQL statement
            preparedStatement.setString(1, buyOffer.getPlayer());
            preparedStatement.setString(2, buyOffer.getMaterial());
            preparedStatement.setDouble(5, buyOffer.getPrice());
            preparedStatement.setInt(6, buyOffer.getTotalqty());
            preparedStatement.setInt(7, buyOffer.getBoughtqty());
            preparedStatement.setDouble(8, buyOffer.getMoney());
            preparedStatement.setString(9, buyOffer.getDate());

            // Execute the SQL statement
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            OfferHelper.sendValidationMessage(cmdSender, "Something went wrong with committing your listing to the database! Contact an administrator.");
            return false;
        }
    }

    // Function to update a single BuyOffer in the database
    public boolean updateBuyOffer(Player cmdSender, BuyOffer buyOffer) {
        return true;
    }

    // Function to update a list of BuyOffers in the database
    public boolean updateBuyOfferList(Player cmdSender, List<BuyOffer> buyOfferList) {
        return true;
    }
}
