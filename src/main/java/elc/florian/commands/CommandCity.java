package elc.florian.commands;

import java.sql.*;
import java.util.*;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import elc.florian.other.InfoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CommandCity implements CommandExecutor {
    static Main main;

    public CommandCity(Main main) {
        CommandCity.main = main;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length == 0) {
                List<String> cityList = new ArrayList<>();
                getCity(cityList);

                sender.sendMessage(ChatColor.BLUE + "Voici la liste des villes du pays: " + ChatColor.GOLD + cityList);
                // return false;
            } else {
                final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                try {
                    final Connection connection = db1Connection.getConnection();

                    if (Objects.equals(args[0], "add") || Objects.equals(args[0], "create")) {
                        createCity(connection, args[1], sender);

                    } else if (Objects.equals(args[0], "join")) {
                        joinCity(connection, args[1], sender);

                    } else if (Objects.equals(args[0], "leave")) {
                        leaveCity(connection, sender);

                    } else if (Objects.equals(args[0], "info")) {
                        infoCity(connection, args[1], sender);

                    } else if (Objects.equals(args[0], "setspawn")) {
                        setSpawnCity(connection, args[1], sender);

                    } else {
                        sender.sendMessage(ChatColor.RED + "No arguments matches with " + args[0]);
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                    sender.sendMessage(ChatColor.RED + "Database connection failed");
                }
            }
        });
        return false;
    }


    private void infoCity(Connection connection, String city, CommandSender sender) {
        final PreparedStatement preparedStatement1;
        try {
            preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs, maire, lv FROM city WHERE name = ?");

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

            if (isInCity(city, sender)) {
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
    }

    private void createCity(Connection connection, String name, CommandSender sender) {
        if (InfoPlayer.playerJoinCity(connection, name, sender)) {
            try {
                final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO city VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                final Player player = (Player) sender;
                final Location location = player.getLocation();
                final long time = System.currentTimeMillis();

                // namePrimary
                preparedStatement.setString(1, name);

                // lv
                preparedStatement.setInt(2, 0);

                // habs_nb
                preparedStatement.setInt(3, 1);

                // habs
                preparedStatement.setString(4, sender.getName());

                // maire
                preparedStatement.setString(5, sender.getName());

                // spawn_world
                preparedStatement.setString(6, Objects.requireNonNull(location.getWorld()).getName());

                // spawn_x
                preparedStatement.setDouble(7, location.getX());

                // spawn_y
                preparedStatement.setDouble(8, location.getY());

                // spawn_z
                preparedStatement.setDouble(9, location.getZ());

                // spawn_yaw
                preparedStatement.setFloat(10, location.getYaw());

                // spawn_pitch
                preparedStatement.setFloat(11, location.getPitch());

                // territory_size
                preparedStatement.setFloat(12, 100);

                // territory_pos1x
                preparedStatement.setInt(13, (int) (location.getX() + 50));

                // territory_pos1y
                preparedStatement.setInt(14, (int) (location.getY() + 50));

                // territory_pos2x
                preparedStatement.setInt(15, (int) (location.getX() - 50));

                // territory_pos2y
                preparedStatement.setInt(16, (int) (location.getY() - 50));

                // created_at
                preparedStatement.setTimestamp(17, new Timestamp(time));

                // updated_at
                preparedStatement.setTimestamp(18, new Timestamp(time));


                preparedStatement.executeUpdate();

                sender.sendMessage(ChatColor.GREEN + name + " a été ajouté à la liste des villes !");
            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "ERREUR durant la creation de la ville");
            }
        }
    }

    private void joinCity(Connection connection, String cityName, CommandSender sender) {
        if (isInCity(cityName, sender)) {
            if (InfoPlayer.playerJoinCity(connection, cityName, sender)) {
                try {
                    final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs FROM city WHERE name = ?");

                    preparedStatement1.setString(1, cityName);

                    final ResultSet resultSet = preparedStatement1.executeQuery();
                    int habs_nb;
                    String habs_name;
                    if (resultSet.next()) {
                        habs_nb = resultSet.getInt(1);
                        habs_name = resultSet.getString(2);

                        final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE city SET habs_nb = ?, habs = ?, updated_at = ? WHERE name = ?");
                        final long time = System.currentTimeMillis();

                        preparedStatement2.setInt(1, habs_nb + 1);
                        preparedStatement2.setString(2, habs_name + " " + sender.getName());
                        preparedStatement2.setTimestamp(3, new Timestamp(time));
                        preparedStatement2.setString(4, cityName);

                        preparedStatement2.executeUpdate();
                    } else {
                        sender.sendMessage(ChatColor.RED + "ERROR ");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                sender.sendMessage(ChatColor.GREEN + "Vous avez rejoint " + cityName + " !");
            }


        } else {
            sender.sendMessage(ChatColor.RED + "La ville " + cityName + " n'existe pas");
        }
    }

    private void leaveCity(Connection connection, CommandSender sender) {
        String city = InfoPlayer.playerLeaveCity(connection, sender);
        if (city != null){
            try {
                final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs, maire FROM city WHERE name = ?");

                preparedStatement1.setString(1, city);

                final ResultSet resultSet = preparedStatement1.executeQuery();
                int habs_nb;
                String habs_name;
                String maire;
                if (resultSet.next()) {
                    habs_nb = resultSet.getInt(1);
                    habs_name = resultSet.getString(2);
                    maire = resultSet.getString(3);

                    if (habs_name.contains(sender.getName() + " ")) {
                        habs_name = habs_name.replaceAll(sender.getName() + " ", "");
                    } else {
                        habs_name = habs_name.replaceAll(sender.getName(), "");
                    }

                    if (maire.contains(sender.getName())) {
                        maire = "";
                    }

                    final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE city SET habs_nb = ?, habs = ?, maire = ?, updated_at = ? WHERE name = ?");
                    final long time = System.currentTimeMillis();

                    preparedStatement2.setInt(1, habs_nb - 1);
                    preparedStatement2.setString(2, habs_name);
                    preparedStatement2.setString(3, maire);
                    preparedStatement2.setTimestamp(4, new Timestamp(time));

                    preparedStatement2.setString(5, city);

                    preparedStatement2.executeUpdate();
                    sender.sendMessage(ChatColor.GREEN + "Vous avez quitté " + city);
                } else {
                    sender.sendMessage(ChatColor.RED + "db ERROR ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isInCity(String city, CommandSender sender){
        List<String> cityList = new ArrayList<>();
        getCity(cityList);

        if (cityList.contains(city)) {
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Cette ville n'existe pas !");
        }

        return cityList.contains(city);
    }

    private static List<String> getCity(List<String> cityList) {
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

    public static void spawnCity(Connection connection, String cityName, CommandSender sender) {
        if (isInCity(cityName, sender)) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Debut de la teleportation ...");
            try {
                final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT spawn_world, spawn_x, spawn_y, spawn_z, spawn_yaw, spawn_pitch FROM city WHERE name = ?");
                preparedStatement1.setString(1, cityName);

                final ResultSet resultSet = preparedStatement1.executeQuery();
                String spawn_world = "0";
                double spawn_x = 0;
                double spawn_y = 0;
                double spawn_z = 0;
                float spawn_yaw = 0;
                float spawn_pitch = 0;

                if (resultSet.next()) {
                    spawn_world = resultSet.getString(1);
                    spawn_x = resultSet.getDouble(2);
                    spawn_y = resultSet.getDouble(3);
                    spawn_z = resultSet.getDouble(4);
                    spawn_yaw = resultSet.getFloat(5);
                    spawn_pitch = resultSet.getFloat(6);
                }

                Player player = (Player) sender;
                World world = Bukkit.getWorld(spawn_world);
                final Location location = new Location(world, spawn_x, spawn_y, spawn_z, spawn_yaw, spawn_pitch);
                player.teleport(location);
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "... le voyage est terminé");

            } catch (SQLException e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "ERROR");
            }
        }
    }

    private void setSpawnCity(Connection connection, String cityName, CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "fonctionnalité en développement");
    }
}
