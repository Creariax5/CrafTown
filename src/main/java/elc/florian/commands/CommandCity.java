package elc.florian.commands;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import elc.florian.other.InfoCity;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
                cityList = getCity(cityList);

                sender.sendMessage(ChatColor.BLUE + "Voici la liste des villes du pays: " + ChatColor.GOLD + cityList);
                // return false;
            } else {

                    Player player = (Player) sender;
                    if (Objects.equals(args[0], "add") || Objects.equals(args[0], "create")) {
                        createCity(args[1], sender);

                    } else if (Objects.equals(args[0], "join")) {
                        joinCity(args[1], player);

                    } else if (Objects.equals(args[0], "leave")) {
                        leaveCity(player);

                    } else if (Objects.equals(args[0], "info")) {
                        String city = args[1];
                        InfoCity infoCity = getInfoCityAuto(city);

                        if (isInCity(city, sender)) {
                            sender.sendMessage(ChatColor.BLUE + "Info de " + city + ":");
                            sender.sendMessage(ChatColor.GOLD + "----------------------");
                            sender.sendMessage(ChatColor.GOLD + "nombre d'habitants: " + infoCity.getHabs_nb());
                            sender.sendMessage(ChatColor.GOLD + "Habitants: " + infoCity.getHabs());
                            sender.sendMessage(ChatColor.GOLD + "Niveau: " + infoCity.getLv());
                            sender.sendMessage(ChatColor.GOLD + "Maire: " + infoCity.getMaire());
                            sender.sendMessage(ChatColor.GOLD + "----------------------");
                        } else {
                            sender.sendMessage(ChatColor.RED + "No city named " + city);
                        }

                    } else if (Objects.equals(args[0], "setspawn")) {
                        setSpawnCity(args[1], sender);

                    } else {
                        sender.sendMessage(ChatColor.RED + "No arguments matches with " + args[0]);
                    }
            }
        });
        return false;
    }


    public static InfoCity getInfoCityAuto(String city) {
        if (main.getInfoPlayer().containsKey(city)) {
            final InfoCity infoCity = main.getInfoCity().get(city);
            return infoCity;
        } else {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();
            try {
                final Connection connection = db1Connection.getConnection();

                final PreparedStatement preparedStatement1;

                preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs, maire, lv, spawn_world, spawn_x, spawn_y, spawn_z, spawn_yaw, spawn_pitch FROM city WHERE name = ?");

                preparedStatement1.setString(1, city);

                final ResultSet resultSet = preparedStatement1.executeQuery();
                int habs_nb = 0;
                String habs_name = null;
                String maire = null;
                int lv = 0;

                String spawn_world = "0";
                double spawn_x = 0;
                double spawn_y = 0;
                double spawn_z = 0;
                float spawn_yaw = 0;
                float spawn_pitch = 0;
                if (resultSet.next()) {
                    habs_nb = resultSet.getInt(1);
                    habs_name = resultSet.getString(2);
                    maire = resultSet.getString(3);
                    lv = resultSet.getInt(4);

                    spawn_world = resultSet.getString(5);
                    spawn_x = resultSet.getDouble(6);
                    spawn_y = resultSet.getDouble(7);
                    spawn_z = resultSet.getDouble(8);
                    spawn_yaw = resultSet.getFloat(9);
                    spawn_pitch = resultSet.getFloat(10);
                }

                InfoCity infoCity = new InfoCity(habs_nb, habs_name, maire, lv, spawn_world, spawn_x, spawn_y, spawn_z, spawn_yaw, spawn_pitch);

                main.getInfoCity().put(city, infoCity);
                return infoCity;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void createCity(String name, CommandSender sender) {
        final DbConnection db1Connection = main.getDbManager().getDb1Connection();
        try {
            final Connection connection = db1Connection.getConnection();

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
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Database connection failed");
        }
    }

    public static void joinCity(String city, Player player) {
        final DbConnection db1Connection = main.getDbManager().getDb1Connection();
        try {
            final Connection connection = db1Connection.getConnection();

            if (isInCity(city, player)) {
                if (InfoPlayer.playerJoinCity(connection, city, player)) {
                    try {
                        UUID uuid = player.getUniqueId();

                        InfoPlayer infoPlayer1 = CommandMarket.getInfoPlayerAuto(uuid);
                        infoPlayer1.setVille(city);
                        main.getInfoPlayer().put(uuid, infoPlayer1);

                        final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs FROM city WHERE name = ?");

                        preparedStatement1.setString(1, city);

                        final ResultSet resultSet = preparedStatement1.executeQuery();
                        int habs_nb;
                        String habs_name;
                        if (resultSet.next()) {
                            habs_nb = resultSet.getInt(1);
                            habs_name = resultSet.getString(2);

                            final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE city SET habs_nb = ?, habs = ?, updated_at = ? WHERE name = ?");
                            final long time = System.currentTimeMillis();

                            habs_nb = habs_nb + 1;
                            habs_name = habs_name + " " + player.getName();

                            preparedStatement2.setInt(1, habs_nb);
                            preparedStatement2.setString(2, habs_name);
                            preparedStatement2.setTimestamp(3, new Timestamp(time));
                            preparedStatement2.setString(4, city);

                            preparedStatement2.executeUpdate();

                            InfoCity infoCity1 = CommandCity.getInfoCityAuto(city);
                            assert infoCity1 != null;
                            infoCity1.setHabs(habs_name);
                            infoCity1.setHabs_nb(habs_nb);

                            main.getInfoCity().put(city, infoCity1);
                            spawnCity(city, player);
                        } else {
                            player.sendMessage(ChatColor.RED + "ERROR ");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(ChatColor.GREEN + "Vous avez rejoint " + city + " !");
                }


            } else {
                player.sendMessage(ChatColor.RED + "La ville " + city + " n'existe pas");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Database connection failed");
        }
    }

    public static void leaveCity(Player player) {
        final DbConnection db1Connection = main.getDbManager().getDb1Connection();
        try {
            final Connection connection = db1Connection.getConnection();

            String city = InfoPlayer.playerLeaveCity(connection, player);
            if (city != null) {
                UUID uuid = player.getUniqueId();

                InfoPlayer infoPlayer1 = CommandMarket.getInfoPlayerAuto(uuid);
                infoPlayer1.setVille("rural");
                main.getInfoPlayer().put(uuid, infoPlayer1);

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

                        if (habs_name.contains(player.getName() + " ")) {
                            habs_name = habs_name.replaceAll(player.getName() + " ", "");
                        } else {
                            habs_name = habs_name.replaceAll(player.getName(), "");
                        }

                        if (maire.contains(player.getName())) {
                            maire = "";
                        }

                        final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE city SET habs_nb = ?, habs = ?, maire = ?, updated_at = ? WHERE name = ?");
                        final long time = System.currentTimeMillis();

                        habs_nb = habs_nb - 1;

                        preparedStatement2.setInt(1, habs_nb);
                        preparedStatement2.setString(2, habs_name);
                        preparedStatement2.setString(3, maire);
                        preparedStatement2.setTimestamp(4, new Timestamp(time));

                        preparedStatement2.setString(5, city);

                        preparedStatement2.executeUpdate();

                        InfoCity infoCity1 = CommandCity.getInfoCityAuto(city);;
                        assert infoCity1 != null;
                        infoCity1.setHabs_nb(habs_nb);
                        infoCity1.setHabs(habs_name);
                        main.getInfoCity().put(city, infoCity1);

                        player.sendMessage(ChatColor.GREEN + "Vous avez quitté " + city);
                    } else {
                        player.sendMessage(ChatColor.RED + "db ERROR ");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Database connection failed");
        }
    }

    private static boolean isInCity(String city, CommandSender sender){
        List<String> cityList = new ArrayList<>();
        cityList = getCity(cityList);

        if (cityList.contains(city)) {
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Cette ville n'existe pas !");
        }

        return cityList.contains(city);
    }

    public static List<String> getCity(List<String> cityList) {
        try {
            String test = main.getCityList().get(0);
            cityList = main.getCityList();

            return cityList;
        } catch (Exception error){

            final DbConnection db1Connection = main.getDbManager().getDb1Connection();
            try {
                final Connection connection = db1Connection.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM city");

                final ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    cityList.add(resultSet.getString(1));
                }
                main.setCityList(cityList);
                return cityList;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("error !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return null;
    }

    public static void spawnCity(String cityName, CommandSender sender) {
        if (isInCity(cityName, sender)) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Debut de la teleportation ...");

            InfoCity infoCity = getInfoCityAuto(cityName);

            assert infoCity != null;
            String spawn_world = infoCity.getSpawn_world();
            double spawn_x = infoCity.getSpawn_x();
            double spawn_y = infoCity.getSpawn_y();
            double spawn_z = infoCity.getSpawn_z();
            float spawn_yaw = infoCity.getSpawn_yaw();
            float spawn_pitch = infoCity.getSpawn_pitch();

            Player player = (Player) sender;
            World world = Bukkit.getWorld(spawn_world);
            final Location location = new Location(world, spawn_x, spawn_y, spawn_z, spawn_yaw, spawn_pitch);
            player.teleport(location);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "... le voyage est terminé");
        }
    }

    private void setSpawnCity(String cityName, CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "fonctionnalité en développement");
    }
}
