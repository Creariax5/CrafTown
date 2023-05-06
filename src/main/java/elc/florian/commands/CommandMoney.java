package elc.florian.commands;

import elc.florian.Main;
import elc.florian.db.DbConnection;
import elc.florian.listener.JoinListener;
import elc.florian.other.InfoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.UUID;

public class CommandMoney implements CommandExecutor {
    private static Main main;

    public CommandMoney(Main main) {
        CommandMoney.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();


            if (args.length == 0) {
                DecimalFormat df = new DecimalFormat("0.00");
                if (main.getInfoPlayer().containsKey(uuid)) {

                    final InfoPlayer infoPlayer = main.getInfoPlayer().get(uuid);
                    sender.sendMessage(ChatColor.DARK_GREEN + "Vous avez " + df.format(infoPlayer.getMoney()) + " sur votre compte");
                } else {
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

                    final InfoPlayer infoPlayer = main.getInfoPlayer().get(uuid);
                    sender.sendMessage(ChatColor.DARK_GREEN + "Vous avez " + df.format(infoPlayer.getMoney()) + " sur votre compte");
                }


            } else {
                sender.sendMessage(ChatColor.RED + "En dev");
            }
        });
        return false;
    }
}


