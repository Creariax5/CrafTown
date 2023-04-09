package elc.florian.other;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.http.WebSocket;
import java.sql.*;
import java.util.UUID;

public class JoinListener implements WebSocket.Listener, Listener {
    private final Main main;

    public JoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        event.setJoinMessage(null);
        String name = player.getName();

        final DbConnection db1Connection = main.getDbManager().getDb1Connection();
        try {
            final Connection connection = db1Connection.getConnection();
            Info_player info_player = getPlayer(connection, uuid);
            if (info_player == null) {
                createPlayer(connection, uuid, name);
                info_player = getPlayer(connection, uuid);
            }

            Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + info_player.getGrade() + ChatColor.AQUA + "] " + player.getName() + ChatColor.GOLD + " s'est réveillé");
            main.getInfo_player().put(uuid, info_player);
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(ChatColor.GOLD + "Un joueur inconnu vient d'arriver");
        }
    }

    private void createPlayer(Connection connection, UUID uuid, String name) {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            final Long time = System.currentTimeMillis();

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, "mendiant");
            preparedStatement.setString(4, "rural");
            preparedStatement.setInt(5, 0);
            preparedStatement.setString(6, "chômeur");
            preparedStatement.setTimestamp(7, new Timestamp(time));
            preparedStatement.setTimestamp(8, new Timestamp(time));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Info_player getPlayer(Connection connection, UUID uuid) {

        String grade;
        String ville;
        int lv;
        String travail;


        Info_player info_player = null;
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT grade, ville, lv, travail FROM player WHERE uuid = ?");

            preparedStatement.setString(1, uuid.toString());
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                grade = (resultSet.getString(1));
                ville = (resultSet.getString(2));
                lv = (resultSet.getInt(3));
                travail = (resultSet.getString(4));

                info_player = new Info_player(uuid, grade, ville, lv, travail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info_player;
    }
}
