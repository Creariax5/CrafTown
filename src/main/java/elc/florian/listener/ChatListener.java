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

        if (main.getInfo_player().containsKey(uuid)) {

            final InfoPlayer infoPlayer = main.getInfo_player().get(uuid);
            event.setFormat(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + infoPlayer.getGrade() + ChatColor.AQUA + "] " + player.getDisplayName() + " -> " + ChatColor.WHITE + event.getMessage());

        } else {
            final DbConnection db1Connection = main.getDbManager().getDb1Connection();

            final Connection connection;
            try {
                connection = db1Connection.getConnection();
                InfoPlayer info_player = JoinListener.getPlayer(connection, uuid);
                main.getInfo_player().put(uuid, info_player);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            final InfoPlayer infoPlayer = main.getInfo_player().get(uuid);
            event.setFormat(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + infoPlayer.getGrade() + ChatColor.AQUA + "] " + player.getDisplayName() + " -> " + ChatColor.WHITE + event.getMessage());
        }
    }
}
