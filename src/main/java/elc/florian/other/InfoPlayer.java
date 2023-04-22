package elc.florian.other;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class InfoPlayer {
    private UUID uuid;
    private String username;
    private String grade;
    private String ville;
    private int lv;
    private String travail;
    private float money;


    public InfoPlayer(UUID uuid, String username, String grade, String ville, int lv, String travail, float money) {
        this.uuid = uuid;
        this.username = username;
        this.grade = grade;
        this.ville = ville;
        this.lv = lv;
        this.travail = travail;
        this.money = money;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getGrade() {
        return grade;
    }

    public String getVille() {
        return ville;
    }

    public int getLv() {
        return lv;
    }

    public String getTravail() {
        return travail;
    }

    public float getMoney() {
        return money;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public void setTravail(String travail) {
        this.travail = travail;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public static boolean playerJoinCity(Connection connection, String villeName, CommandSender sender) {
        try {
            final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT ville FROM player WHERE pseudo = ?");

            preparedStatement1.setString(1, sender.getName());

            final ResultSet resultSet = preparedStatement1.executeQuery();

            if (resultSet.next()) {
                String currentVilleName = resultSet.getString(1);
                if (Objects.equals(currentVilleName, "rural")) {
                    final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE player SET ville = ?, updated_at = ? WHERE pseudo = ?");
                    final long time = System.currentTimeMillis();

                    preparedStatement2.setString(1, villeName);
                    preparedStatement2.setTimestamp(2, new Timestamp(time));
                    preparedStatement2.setString(3, sender.getName());

                    preparedStatement2.executeUpdate();

                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Vous etes déja dans la ville " + currentVilleName + ", quittez la pour en rejoindre ou en créer une autre");
                    return false;
                }


            } else {
                sender.sendMessage(ChatColor.RED + "ERROR ");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String playerLeaveCity(Connection connection, CommandSender sender) {
        try {
            final PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT ville FROM player WHERE pseudo = ?");

            preparedStatement1.setString(1, sender.getName());

            final ResultSet resultSet = preparedStatement1.executeQuery();

            if (resultSet.next()) {
                String currentVilleName = resultSet.getString(1);
                if (!Objects.equals(currentVilleName, "rural")) {
                    final PreparedStatement preparedStatement2;
                    try {
                        preparedStatement2 = connection.prepareStatement("UPDATE player SET ville = ?, updated_at = ? WHERE pseudo = ?");

                        final long time = System.currentTimeMillis();

                        preparedStatement2.setString(1, "rural");
                        preparedStatement2.setTimestamp(2, new Timestamp(time));
                        preparedStatement2.setString(3, sender.getName());

                        preparedStatement2.executeUpdate();
                        return currentVilleName;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Vous n'etes pas dans une ville, vous ne pouvez pas la quittez");
                    return null;
                }


            } else {
                sender.sendMessage(ChatColor.RED + "ERROR ");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
