package elc.florian.other;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ChatListener implements Listener {
    private final Main main;

    public ChatListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if (main.getInfo_player().containsKey(uuid)) {

            final Info_player info_player = main.getInfo_player().get(uuid);
            event.setFormat(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + info_player.getGrade() + ChatColor.AQUA + "] " + player.getDisplayName() + " -> " + ChatColor.WHITE + event.getMessage());
        } else {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();

            final Connection connection;
            try {
                connection = db1Connection.getConnection();
                getPlayer(connection, uuid);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            final Info_player info_player = main.getInfo_player().get(uuid);
            event.setFormat(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + info_player.getGrade() + ChatColor.AQUA + "] " + player.getDisplayName() + " -> " + ChatColor.WHITE + event.getMessage());
            // event.setFormat(ChatColor.RED + event.getMessage());
        }
    }

    private void getPlayer(Connection connection, UUID uuid) {

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

            main.getInfo_player().put(uuid, info_player);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
