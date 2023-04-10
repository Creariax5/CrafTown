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
                if (main.getInfo_player().containsKey(uuid)) {

                    final InfoPlayer infoPlayer = main.getInfo_player().get(uuid);
                    sender.sendMessage(ChatColor.DARK_GREEN + "Vous avez " + infoPlayer.getMoney() + " sur votre compte");
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
                    sender.sendMessage(ChatColor.DARK_GREEN + "Vous avez " + infoPlayer.getMoney() + " sur votre compte");
                }


            } else {
                sender.sendMessage(ChatColor.RED + "En dev");
            }
        });
        return false;
    }
}


