package elc.florian.commands;

import java.sql.*;
import java.util.*;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import elc.florian.other.Info_player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCity implements CommandExecutor {
    private final Main main;

    public CommandCity(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length == 0) {
                List<String> cityList = new ArrayList<String>();
                getCity(cityList);

                sender.sendMessage(ChatColor.BLUE + "Voici la liste des villes du pays: " + ChatColor.GOLD + cityList.toString());
                // return false;
            } else {
                if (Objects.equals(args[0], "add") || Objects.equals(args[0], "create")) {
                    args[0] = "";
                    String name = String.join("", args);
                    System.out.println(name);

                    final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                    try {
                        final Connection connection = db1Connection.getConnection();
                        createCity(connection, name, sender);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sender.sendMessage(ChatColor.RED + "ERREUR durant la creation de la ville");
                    }

                } else if (Objects.equals(args[0], "join")) {
                    List<String> cityList = new ArrayList<String>();
                    getCity(cityList);

                    args[0] = "";
                    String city = String.join("", args);
                    if (cityList.contains(city)) {
                        // sender.sendMessage(ChatColor.GREEN + "Votre demande pour rejoindre " + city + " a été a été envoyée.");
                        // sender.sendMessage("");

                        final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                        try {
                            final Connection connection = db1Connection.getConnection();
                            String user_name = sender.getName();
                            joinCity(connection, city, sender);
                            sender.sendMessage(ChatColor.GREEN + "Vous avez été accepté dans " + city + " vous avez donc rejoint la ville !");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            sender.sendMessage(ChatColor.RED + "Vous ne pouvez pas rejoidre la ville " + args[0] + " pour le moment");
                        }

                        /*sender.sendMessage("");
                        sender.sendMessage("" + ChatColor.DARK_GREEN + ChatColor.UNDERLINE + "Message de bienvenue de " + city + ":");
                        sender.sendMessage("");
                        sender.sendMessage(ChatColor.DARK_GREEN + "Nous te souhaitons la bienvenue parmi nous.\n" +
                                "Nous sommes très content de t'accueillir a " + city + " !\n" +
                                "N'hésite pas à solliciter le maire si tu as besoin de quoi que ce soit.\n "); */
                    } else {
                        sender.sendMessage(ChatColor.RED + "La ville " + args[0] + " n'existe pas");
                    }

                } else if (Objects.equals(args[0], "leave")) {
                    String city = args[1];
                    final DbConnection db1Connection = main.getDbManager().getDb1Connection();
                    try {
                        final Connection connection = db1Connection.getConnection();
                        leaveCity(connection, city, sender);
                        sender.sendMessage(ChatColor.GREEN + "Vous avez quitté " + city);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sender.sendMessage(ChatColor.RED + "ERREUR");
                    }

                } else if (Objects.equals(args[0], "info")) {
                    List<String> cityList = new ArrayList<>();
                    getCity(cityList);
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
                            sender.sendMessage(ChatColor.BLUE + "Info de " + city + ":");
                            sender.sendMessage(ChatColor.GOLD + "----------------------");
                            sender.sendMessage(ChatColor.GOLD + "nombre d'habitants: " + habs_nb);
                            sender.sendMessage(ChatColor.GOLD + "Habitants: " + habs_name);
                            sender.sendMessage(ChatColor.GOLD + "Niveau: " + lv);
                            sender.sendMessage(ChatColor.GOLD + "Maire: " + "NULL");
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
        });
        return false;
    }

    private void leaveCity(Connection connection, String city, CommandSender sender) {
        String rural = "rural";
        if (Info_player.playerJoinCity(connection, rural, sender)) {
            try {
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

                    preparedStatement2.setInt(1, habs_nb - 1);
                    preparedStatement2.setString(2, habs_name.replaceAll(sender.getName() + " ", ""));
                    preparedStatement2.setTimestamp(3, new Timestamp(time));
                    preparedStatement2.setString(4, city);

                    preparedStatement2.executeUpdate();
                } else {
                    sender.sendMessage(ChatColor.RED + "ERROR ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createCity(Connection connection, String name, CommandSender sender) {
        if (Info_player.playerJoinCity(connection, name, sender)) {
            try {
                final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO city VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                final Player player = (Player) sender;
                final Location location = player.getLocation();
                final Long time = System.currentTimeMillis();

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
                preparedStatement.setString(6, location.getWorld().getName());

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

    private void joinCity(Connection connection, String name, CommandSender sender) {
        if (Info_player.playerJoinCity(connection, name, sender)) {
            try {
                final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT habs_nb, habs FROM city WHERE name = ?");

                preparedStatement1.setString(1, name);

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
                    preparedStatement2.setString(4, name);

                    preparedStatement2.executeUpdate();
                } else {
                    sender.sendMessage(ChatColor.RED + "ERROR ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
