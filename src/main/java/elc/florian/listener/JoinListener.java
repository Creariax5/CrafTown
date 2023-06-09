package elc.florian.listener;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import elc.florian.other.InfoPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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
            InfoPlayer info_player = getPlayer(connection, uuid);
            if (info_player == null) {
                createPlayer(connection, uuid, name);
                info_player = getPlayer(connection, uuid);
            }

            Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + info_player.getGrade() + ChatColor.AQUA + "] " + player.getName() + ChatColor.GOLD + " s'est réveillé");

            TextComponent message = new TextComponent(ChatColor.YELLOW + "Clickable /menu");
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.YELLOW + "Execute /menu")));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu"));
            player.spigot().sendMessage(message);

            main.getInfoPlayer().put(uuid, info_player);
            main.getUsernameToUUID().put(info_player.getUsername(), uuid);
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(ChatColor.GOLD + "Un joueur inconnu vient d'arriver");
        }
    }

    private void createPlayer(Connection connection, UUID uuid, String name) {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            final Long time = System.currentTimeMillis();

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, "mendiant");
            preparedStatement.setString(4, "rural");
            preparedStatement.setInt(5, 0);
            preparedStatement.setString(6, "chômeur");
            preparedStatement.setInt(7, 600);
            preparedStatement.setTimestamp(8, new Timestamp(time));
            preparedStatement.setTimestamp(9, new Timestamp(time));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static InfoPlayer getPlayer(Connection connection, UUID uuid) {

        String username;
        String grade;
        String ville;
        int lv;
        String travail;
        int money;


        InfoPlayer info_player = null;
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT pseudo, grade, ville, lv, travail, money FROM player WHERE uuid = ?");

            preparedStatement.setString(1, uuid.toString());
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                username = (resultSet.getString(1));
                grade = (resultSet.getString(2));
                ville = (resultSet.getString(3));
                lv = (resultSet.getInt(4));
                travail = (resultSet.getString(5));
                money = (resultSet.getInt(6));

                info_player = new InfoPlayer(uuid, username, grade, ville, lv, travail, money);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info_player;
    }
}
