package database;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
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
                price REAL NOT NULL,
                date TEXT NOT NULL,
                quantitytosell INTEGER NOT NULL,
                quantitytoclaim INTEGER NOT NULL)
                """);
        statement.execute("""
                CREATE TABLE IF NOT EXISTS buyoffers (
                uuid INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
                player TEXT NOT NULL,
                material TEXT NOT NULL,
                price REAL NOT NULL,
                date TEXT NOT NULL,
                quantitytobuy INTEGER NOT NULL,
                quantitytoclaim INTEGER NOT NULL)
                """);
        statement.close();
    }

    public void addSellOfferToDB(Player player, Material material, double price, String date, int qty) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO selloffers (player, material, price, date, quantitytosell, quantitytoclaim) VALUES (?, ?, ?, ?, ?, ?)")) {
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
}
