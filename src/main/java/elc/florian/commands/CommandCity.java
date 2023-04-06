package elc.florian.commands;

import java.sql.*;
import java.util.*;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCity implements CommandExecutor {
    private Main main;

    public CommandCity(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length == 0) {
                List<String> cityList = new ArrayList<String>();
                cityList = getCity(cityList);

                sender.sendMessage(ChatColor.BLUE + "Voici la liste des villes du pays: " + ChatColor.GOLD + cityList.toString());
                // return false;
            } else {
                if (Objects.equals(args[0], "add")) {
                    args[0] = "";
                    String name = String.join("", args);
                    System.out.println(name);

                    final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                    try {
                        final Connection connection = db1Connection.getConnection();
                        createCity(connection, name);
                        sender.sendMessage(ChatColor.GREEN + name + " a été ajouté à la liste des villes !");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (Objects.equals(args[0], "join")) {
                    List<String> cityList = new ArrayList<String>();
                    cityList = getCity(cityList);

                    args[0] = "";
                    String city = String.join("", args);
                    if (cityList.contains(city)) {
                        // sender.sendMessage(ChatColor.GREEN + "Votre demande pour rejoindre " + city + " a été a été envoyée.");
                        // sender.sendMessage("");

                        final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                        try {
                            final Connection connection = db1Connection.getConnection();
                            String user_name = sender.getName();
                            joinCity(connection, city, user_name);
                            sender.sendMessage(ChatColor.GREEN + "Vous avez été accepté dans " + city + " vous avez donc rejoint la ville !");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Bukkit.broadcastMessage(ChatColor.RED + "Vous ne pouvez pas rejoidre la ville " + args[0] + " pour le moment");
                        }

                        /*sender.sendMessage("");
                        sender.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.UNDERLINE + "Message de bienvenue de " + city + ":");
                        sender.sendMessage("");
                        sender.sendMessage(ChatColor.DARK_GREEN + "Nous te souhaitons la bienvenue parmi nous.\n" +
                                "Nous sommes très content de t'accueillir a " + city + " !\n" +
                                "N'hésite pas à solliciter le maire si tu as besoin de quoi que ce soit.\n "); */
                    } else {
                        Bukkit.broadcastMessage(ChatColor.RED + "La ville " + args[0] + " n'existe pas");
                    }

                } else if (Objects.equals(args[0], "info")) {
                    List cityList = new ArrayList<String>();
                    cityList = getCity(cityList);
                    String city = args[1];

                    final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                    final Connection connection;
                    try {
                        connection = db1Connection.getConnection();

                        final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs, lv FROM city WHERE name = ?");

                        preparedStatement1.setString(1, city);

                        final ResultSet resultSet = preparedStatement1.executeQuery();
                        int habs_nb = 0;
                        String habs_name = null;
                        int lv = 0;
                        if (resultSet.next()) {
                            habs_nb = resultSet.getInt(1);
                            habs_name = resultSet.getString(2);
                            lv = resultSet.getInt(3);
                        }

                        if (cityList.contains(city)) {
                            Bukkit.broadcastMessage(ChatColor.BLUE + "Info de " + city + ":");
                            Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------");
                            Bukkit.broadcastMessage(ChatColor.GOLD + "nombre d'habitants: " + habs_nb);
                            Bukkit.broadcastMessage(ChatColor.GOLD + "Habitants: " + habs_name);
                            Bukkit.broadcastMessage(ChatColor.GOLD + "Niveau: " + lv);
                            Bukkit.broadcastMessage(ChatColor.GOLD + "Maire: " + "NULL");
                            Bukkit.broadcastMessage(ChatColor.GOLD + "----------------------");
                        } else {
                            Bukkit.broadcastMessage(ChatColor.RED + "No city named " + city);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else {
                    Bukkit.broadcastMessage(ChatColor.RED + "No arguments matches with " + args[0]);
                }
            }
        });
        return false;
    }

    private void createCity(Connection connection, String name) {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO city VALUES (?, ?, ?, ?, ?, ?, ?)");
            final Long time = System.currentTimeMillis();

            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setString(5, "Les habitants");
            preparedStatement.setTimestamp(6, new Timestamp(time));
            preparedStatement.setTimestamp(7, new Timestamp(time));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void joinCity(Connection connection, String name, String user_name) {
        try {
            final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs FROM city WHERE name = ?");

            preparedStatement1.setString(1, name);

            final ResultSet resultSet = preparedStatement1.executeQuery();
            int habs_nb = 0;
            String habs_name = null;
            if (resultSet.next()) {
                habs_nb = resultSet.getInt(1);
                habs_name = resultSet.getString(2);

                final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE city SET habs_nb = ?, habs = ?, updated_at = ? WHERE name = ?");
                final long time = System.currentTimeMillis();

                preparedStatement2.setInt(1, habs_nb + 1);
                preparedStatement2.setString(2, habs_name + " " + user_name);
                preparedStatement2.setTimestamp(3, new Timestamp(time));
                preparedStatement2.setString(4, name);

                preparedStatement2.executeUpdate();
            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "ERROR ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<String> getCity(List<String> cityList) {
        final DbConnection db1Connection = main.getDbManager().getDb1Connection();
        try {
            final Connection connection = db1Connection.getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM city");

            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cityList.add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }
}
