package elc.florian.listener;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import elc.florian.other.InfoPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.Connection;
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

        if (!main.getInfoPlayer().containsKey(uuid)) {

            final DbConnection db1Connection = main.getDbManager().getDb1Connection();

            final Connection connection;
            try {
                connection = db1Connection.getConnection();
                InfoPlayer info_player = JoinListener.getPlayer(connection, uuid);
                main.getInfoPlayer().put(uuid, info_player);
                main.getUsernameToUUID().put(info_player.getUsername(), uuid);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        final InfoPlayer infoPlayer = main.getInfoPlayer().get(uuid);
        event.setFormat(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + infoPlayer.getGrade() + ChatColor.AQUA + "] " + player.getDisplayName() + " -> " + ChatColor.WHITE + event.getMessage());
    }
}
