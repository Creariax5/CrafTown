package elc.florian.other;

import elc.florian.db.DbConnection;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class tmp {
    final DbConnection db1Connection = main.getDbManager().getDb1Connection();
    final Connection connection = db1Connection.getConnection();
    if (Objects.equals(args[0], "add") || Objects.equals(args[0], "create")) {
        args[0] = "";
        String name = String.join("", args);
        System.out.println(name);

        try {
            createCity(connection, name, sender);
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "ERREUR durant la creation de la ville");
        }

    } else if (Objects.equals(args[0], "join")) {
        String city = args[1];
        if (isInCity(city)) {
            try {
                final Connection connection = db1Connection.getConnection();
                String user_name = sender.getName();
                joinCity(connection, city, sender);
                sender.sendMessage(ChatColor.GREEN + "Vous avez été accepté dans " + city + " vous avez donc rejoint la ville !");
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas rejoidre la ville " + args[0] + " pour le moment");
            }

        } else {
            sender.sendMessage(ChatColor.RED + "La ville " + args[0] + " n'existe pas");
        }

    } else if (Objects.equals(args[0], "leave")) {
        try {
            final Connection connection = db1Connection.getConnection();
            String city = leaveCity(connection, sender);
            sender.sendMessage(ChatColor.GREEN + "Vous avez quitté " + city);
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "ERREUR");
        }

    } else if (Objects.equals(args[0], "info")) {
        String city = args[1];

        final Connection connection;
        try {
            connection = db1Connection.getConnection();

            final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT habs_nb, maire, habs, lv FROM city WHERE name = ?");

            preparedStatement1.setString(1, city);

            final ResultSet resultSet = preparedStatement1.executeQuery();
            int habs_nb = 0;
            String habs_name = null;
            String maire = null;
            int lv = 0;
            if (resultSet.next()) {
                habs_nb = resultSet.getInt(1);
                habs_name = resultSet.getString(2);
                maire = resultSet.getString(3);
                lv = resultSet.getInt(4);
            }

            if (isInCity(city)) {
                sender.sendMessage(ChatColor.BLUE + "Info de " + city + ":");
                sender.sendMessage(ChatColor.GOLD + "----------------------");
                sender.sendMessage(ChatColor.GOLD + "nombre d'habitants: " + habs_nb);
                sender.sendMessage(ChatColor.GOLD + "Habitants: " + habs_name);
                sender.sendMessage(ChatColor.GOLD + "Niveau: " + lv);
                sender.sendMessage(ChatColor.GOLD + "Maire: " + maire);
                sender.sendMessage(ChatColor.GOLD + "----------------------");
            } else {
                sender.sendMessage(ChatColor.RED + "No city named " + city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    } else {
        sender.sendMessage(ChatColor.RED + "No arguments matches with " + args[0]);
    }
}
