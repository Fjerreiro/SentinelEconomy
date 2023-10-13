package database;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import objects.SellOffer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.*;

public class Database {

    private final Connection connection;

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Database(String path) throws SQLException {
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
                leftqty INTEGER NOT NULL,
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
                leftqty INTEGER NOT NULL,
                money REAL DEFAULT 0,
                date TEXT NOT NULL)
                """);
        statement.close();
    }

    public boolean addSellOfferToDB(Player player, SellOffer sellOffer) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO selloffers (player, material, nbt, custom, price, totalqty, leftqty, money, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, sellOffer.getPlayer());
            preparedStatement.setString(2, sellOffer.getMaterial());
            preparedStatement.setDouble(5, sellOffer.getPrice());
            preparedStatement.setInt(6, sellOffer.getTotalqty());
            preparedStatement.setInt(7, 0);
            preparedStatement.setInt(8, 0);
            preparedStatement.setString(9, sellOffer.getDate());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Something went wrong with committing your listing to the database! Contact an administrator.", NamedTextColor.WHITE));
            player.sendMessage(message);
            System.out.println(e);
            return false;
        }
    }


    public void addBuyOfferToDB(Player player, Material material, double price, String date, int qty) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO buyoffers (player, material, price, date, quantitytobuy, quantitytoclaim) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, material.name());
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, date);
            preparedStatement.setInt(5, qty);
            preparedStatement.setInt(6, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Something went wrong with committing your listing to the database! Contact an administrator.", NamedTextColor.WHITE));
            player.sendMessage(message);
            System.out.println(e);
        }
    }

    public void immediateSale(Player player, SellOffer sellOffer) {

        String material = sellOffer.getMaterial();
        int totalQty = sellOffer.getTotalqty();
        int soldQty = sellOffer.getSoldQty();
        int leftQty = totalQty - soldQty;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM buyoffers WHERE material = ? AND leftqty > 0 ORDER BY price DESC, date ASC")) {
            preparedStatement.setString(1, material);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next() && leftQty > 0) {
                int resultLeftQty = resultSet.getInt("leftqty");
                double resultPrice = resultSet.getDouble("price");






            }
        } catch (SQLException e) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Something went wrong with committing your listing to the database! Contact an administrator.", NamedTextColor.WHITE));
            player.sendMessage(message);
            System.out.println(e);
        }
    }
}